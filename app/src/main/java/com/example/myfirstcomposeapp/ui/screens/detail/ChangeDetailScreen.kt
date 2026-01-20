package com.example.myfirstcomposeapp.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfirstcomposeapp.model.ChangeItem
import com.example.myfirstcomposeapp.model.ChangeLog
import com.example.myfirstcomposeapp.model.ChangeStatus
import com.example.myfirstcomposeapp.ui.common.TextInputDialog
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeDetailScreen(
    id: String,
    vm: ChangeViewModel,
    onBack: () -> Unit
) {
    val item = vm.observeById(id).collectAsStateWithLifecycle().value
    val logs = vm.observeLogs(id).collectAsStateWithLifecycle().value

    var showActorDialog by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }
    var showReopenDialog by remember { mutableStateOf(false) }

    var actor by remember { mutableStateOf(vm.currentUser) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("变化点详情") },
                navigationIcon = { IconButton(onClick = onBack) { Text("←") } },
                actions = {
                    TextButton(onClick = { showActorDialog = true }) {
                        Text("处理人：$actor")
                    }
                }
            )
        }
    ) { padding ->

        if (item == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("未找到记录")
            }
            return@Scaffold
        }

        val actions = vm.allowedActions(item)

        Column(Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { HeaderCard(item) }
                item { BasicInfoCard(item) }
                item { ContentCard(item) }

                item {
                    Text(
                        "操作记录",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                items(logs) { log -> LogCard(log) }

                item { Spacer(Modifier.height(96.dp)) }
            }

            // 底部动作（UI-only：直接调用 vm 的流程方法）
            Card(Modifier.padding(12.dp)) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("当前状态：${statusText(item.status)}  |  角色：${vm.currentRole.displayName}")
                    if (actions.isEmpty()) {
                        Text("当前无可用动作", style = MaterialTheme.typography.bodySmall)
                    } else {
                        actions.forEach { act ->
                            Button(onClick = {
                                when (act) {
                                    com.example.myfirstcomposeapp.model.ActionType.SUBMIT -> vm.submit(item.id, actor)
                                    com.example.myfirstcomposeapp.model.ActionType.CONFIRM -> vm.confirm(item.id, actor)
                                    com.example.myfirstcomposeapp.model.ActionType.REJECT -> showRejectDialog = true
                                    com.example.myfirstcomposeapp.model.ActionType.RELEASE -> vm.release(item.id, actor)
                                    com.example.myfirstcomposeapp.model.ActionType.CLOSE -> vm.close(item.id, actor)
                                    com.example.myfirstcomposeapp.model.ActionType.REOPEN -> showReopenDialog = true
                                    else -> {}
                                }
                            }) {
                                Text(actionText(act.name))
                            }
                        }
                    }
                }
            }
        }

        if (showActorDialog) {
            TextInputDialog(
                title = "设置处理人",
                placeholder = "输入处理人",
                initialValue = actor,
                onCancel = { showActorDialog = false },
                onConfirm = {
                    actor = it.trim().ifBlank { actor }
                    showActorDialog = false
                }
            )
        }

        if (showRejectDialog) {
            TextInputDialog(
                title = "驳回原因",
                placeholder = "请输入原因",
                initialValue = "",
                onCancel = { showRejectDialog = false },
                onConfirm = { reason ->
                    vm.reject(item.id, actor, reason.trim())
                    showRejectDialog = false
                }
            )
        }

        if (showReopenDialog) {
            TextInputDialog(
                title = "重开原因",
                placeholder = "请输入原因（可选）",
                initialValue = "",
                onCancel = { showReopenDialog = false },
                onConfirm = { reason ->
                    vm.reopen(item.id, actor, reason.trim())
                    showReopenDialog = false
                }
            )
        }
    }
}

@Composable
private fun HeaderCard(item: ChangeItem) {
    Card {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("ID：${item.id}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun BasicInfoCard(item: ChangeItem) {
    val df = remember { SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()) }
    Card {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("创建人：${item.creator}")
            Text("创建时间：${df.format(Date(item.createdAt))}")
            Text("更新时间：${df.format(Date(item.updatedAt))}")
            Text("类型：${item.type.name}   紧急：${if (item.urgent) "是" else "否"}")
            Text("产线：${item.line}  设备：${item.equipment}  工序：${item.process}")
        }
    }
}

@Composable
private fun ContentCard(item: ChangeItem) {
    Card {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("变化点内容", fontWeight = FontWeight.SemiBold)
            Text(item.content)
        }
    }
}

@Composable
private fun LogCard(log: ChangeLog) {
    val df = remember { SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()) }
    Card {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("${log.action.name}  |  ${log.actor}", fontWeight = FontWeight.SemiBold)
            Text(df.format(Date(log.at)), style = MaterialTheme.typography.bodySmall)
            if (log.note.isNotBlank()) Text(log.note)
        }
    }
}

private fun statusText(status: ChangeStatus): String = when (status) {
    ChangeStatus.DRAFT -> "草稿"
    ChangeStatus.SUBMITTED -> "已提交"
    ChangeStatus.CONFIRMED -> "已确认"
    ChangeStatus.RELEASED -> "已放行"
    ChangeStatus.CLOSED -> "已关闭"
    ChangeStatus.REJECTED -> "已驳回"
    ChangeStatus.REOPENED -> "已重开"
}

private fun actionText(actionName: String): String =
    when (actionName) {
        "SUBMIT" -> "提交"
        "CONFIRM" -> "确认"
        "REJECT" -> "驳回"
        "RELEASE" -> "放行"
        "CLOSE" -> "关闭"
        "REOPEN" -> "重开"
        else -> actionName
    }