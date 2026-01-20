package com.example.myfirstcomposeapp.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myfirstcomposeapp.data.repository.ChangeRepository
import com.example.myfirstcomposeapp.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

enum class AppRole(val displayName: String) {
    CREATOR("发起人"),
    QA("质量"),
    PROCESS("工艺"),
    MANAGER("主管")
}

class ChangeViewModel(
    private val repo: ChangeRepository
) : ViewModel() {

    // 当前用户（演示）
    var currentUser by mutableStateOf("demo")
        private set
    var currentRole by mutableStateOf(AppRole.CREATOR)
        private set

    fun setUser(name: String, role: AppRole) {
        currentUser = name.trim().ifBlank { "demo" }
        currentRole = role
    }

    // 选中项
    var selectedId by mutableStateOf<String?>(null)
        private set
    fun select(id: String) { selectedId = id }
    fun clearSelection() { selectedId = null }

    // ====== 筛选条件（Day10：新增 filterCreator）======
    var keyword by mutableStateOf("")
    var filterCreator by mutableStateOf("")
    var urgentOnly by mutableStateOf(false)
    var selectedType by mutableStateOf<ChangeType?>(null)
    var filterLine by mutableStateOf("")
    var filterEquipment by mutableStateOf("")
    var filterProcess by mutableStateOf("")
    var startDate by mutableStateOf("") // yyyy-MM-dd（更新时间）
    var endDate by mutableStateOf("")

    // 全量列表
    private val allItems: StateFlow<List<ChangeItem>> =
        repo.allItems.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun findById(id: String): ChangeItem? = allItems.value.firstOrNull { it.id == id }

    // 统计
    data class Stats(val total: Int, val urgent: Int, val byStatus: Map<ChangeStatus, Int>)
    val stats: StateFlow<Stats> =
        allItems.map { list ->
            Stats(
                total = list.size,
                urgent = list.count { it.urgent },
                byStatus = ChangeStatus.entries.associateWith { s -> list.count { it.status == s } }
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Stats(0, 0, emptyMap()))

    // ====== Day9 权限能力输出 ======
    fun allowedActions(item: ChangeItem): Set<ActionType> =
        Permission.allowedActions(currentRole, item.status)

    fun canDo(item: ChangeItem, action: ActionType): Boolean =
        Permission.can(currentRole, item.status, action)

    fun todoStatusesForRole(): List<ChangeStatus> = Permission.todoStatuses(currentRole)

    val todoItems: StateFlow<List<ChangeItem>> =
        allItems.map { list ->
            val statuses = todoStatusesForRole().toSet()
            list.filter { it.status in statuses }
                .sortedWith(compareByDescending<ChangeItem> { it.urgent }.thenByDescending { it.updatedAt })
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // ====== Day10：筛选 Flow（加入 filterCreator）======
    private val keywordFlow = snapshotFlow { keyword }
    private val creatorFlow = snapshotFlow { filterCreator }
    private val urgentFlow = snapshotFlow { urgentOnly }
    private val typeFlow = snapshotFlow { selectedType }
    private val lineFlow = snapshotFlow { filterLine }
    private val equipmentFlow = snapshotFlow { filterEquipment }
    private val processFlow = snapshotFlow { filterProcess }
    private val startDateFlow = snapshotFlow { startDate }
    private val endDateFlow = snapshotFlow { endDate }

    private val step1: Flow<List<ChangeItem>> =
        combine(allItems, keywordFlow, creatorFlow, urgentFlow, typeFlow) { items, kw, creator, urgent, type ->
            val kwTrim = kw.trim()
            val creatorTrim = creator.trim()

            items.asSequence()
                // 关键字（变化点/内容/产线/设备/工序/创建人）
                .filter { item ->
                    if (kwTrim.isEmpty()) true else (
                            item.title.contains(kwTrim, true) ||
                                    item.content.contains(kwTrim, true) ||
                                    item.creator.contains(kwTrim, true) ||
                                    item.line.contains(kwTrim, true) ||
                                    item.equipment.contains(kwTrim, true) ||
                                    item.process.contains(kwTrim, true)
                            )
                }
                // 创建人（精确筛选）
                .filter { item ->
                    if (creatorTrim.isEmpty()) true else item.creator.contains(creatorTrim, true)
                }
                // 紧急
                .filter { item -> if (urgent) item.urgent else true }
                // 类型
                .filter { item -> type?.let { item.type == it } ?: true }
                .toList()
        }

    private val step2: Flow<List<ChangeItem>> =
        combine(step1, lineFlow, equipmentFlow, processFlow) { items, line, eq, proc ->
            val lineTrim = line.trim()
            val eqTrim = eq.trim()
            val procTrim = proc.trim()
            items.asSequence()
                .filter { item -> if (lineTrim.isNotEmpty()) item.line.contains(lineTrim, true) else true }
                .filter { item -> if (eqTrim.isNotEmpty()) item.equipment.contains(eqTrim, true) else true }
                .filter { item -> if (procTrim.isNotEmpty()) item.process.contains(procTrim, true) else true }
                .toList()
        }

    private val step3: Flow<List<ChangeItem>> =
        combine(step2, startDateFlow, endDateFlow) { items, s, e ->
            val startMs = parseDateToStartMs(s)
            val endMs = parseDateToEndMs(e)
            items.filter { item ->
                val t = item.updatedAt
                (startMs == null || t >= startMs) && (endMs == null || t <= endMs)
            }
        }

    val filteredItems: StateFlow<List<ChangeItem>> =
        step3.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // 详情 & 日志
    fun observeById(id: String): StateFlow<ChangeItem?> =
        repo.observeById(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun observeLogs(id: String): StateFlow<List<ChangeLog>> =
        repo.observeLogs(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // CRUD
    fun add(draft: ChangeDraft) = viewModelScope.launch { repo.add(draft) }
    fun update(id: String, draft: ChangeDraft) = viewModelScope.launch { repo.update(id, draft) }
    fun delete(id: String) = viewModelScope.launch { repo.delete(id) }

    // 流程动作
    fun submit(id: String, actor: String) = viewModelScope.launch { repo.submit(id, actor) }
    fun confirm(id: String, actor: String) = viewModelScope.launch { repo.confirm(id, actor) }
    fun reject(id: String, actor: String, reason: String) = viewModelScope.launch { repo.reject(id, actor, reason) }
    fun release(id: String, actor: String) = viewModelScope.launch { repo.release(id, actor) }
    fun close(id: String, actor: String) = viewModelScope.launch { repo.close(id, actor) }
    fun reopen(id: String, actor: String, reason: String) = viewModelScope.launch { repo.reopen(id, actor, reason) }

    // 日期解析（yyyy-MM-dd）
    private fun parseDateToStartMs(s: String): Long? {
        val t = s.trim()
        if (t.isEmpty()) return null
        return runCatching {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.parse(t)?.time
        }.getOrNull()
    }

    private fun parseDateToEndMs(s: String): Long? {
        val t = s.trim()
        if (t.isEmpty()) return null
        return runCatching {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val ms = sdf.parse(t)?.time ?: return@runCatching null
            ms + 24 * 60 * 60 * 1000 - 1
        }.getOrNull()
    }
}

class ChangeViewModelFactory(private val repo: ChangeRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ChangeViewModel(repo) as T
}