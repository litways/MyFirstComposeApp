package com.example.myfirstcomposeapp.data.repository

import com.example.myfirstcomposeapp.data.db.ChangeDao
import com.example.myfirstcomposeapp.data.db.ChangeEntity
import com.example.myfirstcomposeapp.data.db.ChangeLogDao
import com.example.myfirstcomposeapp.data.db.ChangeLogEntity
import com.example.myfirstcomposeapp.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class ChangeRepository(
    private val changeDao: ChangeDao,
    private val logDao: ChangeLogDao
) {

    val allItems: Flow<List<ChangeItem>> =
        changeDao.observeAll().map { list -> list.map { it.toModel() } }

    fun observeById(id: String): Flow<ChangeItem?> =
        changeDao.observeAll().map { list ->
            list.firstOrNull { it.id == id }?.toModel()
        }

    fun observeLogs(changeId: String): Flow<List<ChangeLog>> =
        logDao.observeByChangeId(changeId).map { list -> list.map { it.toModel() } }

    suspend fun getById(id: String): ChangeItem? =
        changeDao.getById(id)?.toModel()

    // 兼容旧 UI：add/update/delete
    suspend fun add(draft: ChangeDraft) {
        saveDraft(draft, actor = draft.creator.ifBlank { "system" }, note = "创建草稿")
    }

    suspend fun update(id: String, draft: ChangeDraft) {
        updateDraft(id, draft, actor = draft.creator.ifBlank { "system" }, note = "编辑草稿")
    }

    suspend fun delete(id: String, actor: String = "system") {
        changeDao.deleteById(id)
        logDao.deleteByChangeId(id)
        // 删除日志也记录一条（可选）
        appendLog(id, ActionType.DELETE, actor, "删除变化点")
    }

    // 正式流程 API
    suspend fun saveDraft(draft: ChangeDraft, actor: String, note: String = ""): String {
        val now = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()

        val entity = ChangeEntity(
            id = id,
            creator = draft.creator.trim(),
            createdAt = now,
            updatedAt = now,
            status = ChangeStatus.DRAFT,
            type = draft.type,
            title = draft.title.trim(),
            content = draft.content.trim(),
            urgent = draft.urgent,
            line = draft.line.trim(),
            equipment = draft.equipment.trim(),
            process = draft.process.trim()
        )
        changeDao.upsert(entity)
        appendLog(id, ActionType.CREATE, actor, note.ifBlank { "创建草稿" })
        return id
    }

    suspend fun updateDraft(id: String, draft: ChangeDraft, actor: String, note: String = "") {
        val old = changeDao.getById(id) ?: return
        val entity = old.copy(
            creator = draft.creator.trim(),
            type = draft.type,
            title = draft.title.trim(),
            content = draft.content.trim(),
            urgent = draft.urgent,
            line = draft.line.trim(),
            equipment = draft.equipment.trim(),
            process = draft.process.trim(),
            updatedAt = System.currentTimeMillis()
        )
        changeDao.upsert(entity)
        appendLog(id, ActionType.EDIT, actor, note.ifBlank { "编辑草稿/信息" })
    }

    suspend fun submit(id: String, actor: String) {
        transition(id, actor, ActionType.SUBMIT, ChangeStatus.SUBMITTED, "提交")
    }

    suspend fun confirm(id: String, actor: String) {
        transition(id, actor, ActionType.CONFIRM, ChangeStatus.CONFIRMED, "确认")
    }

    suspend fun reject(id: String, actor: String, reason: String) {
        transition(id, actor, ActionType.REJECT, ChangeStatus.REJECTED, "驳回：$reason")
    }

    suspend fun release(id: String, actor: String) {
        transition(id, actor, ActionType.RELEASE, ChangeStatus.RELEASED, "放行/发布")
    }

    suspend fun close(id: String, actor: String) {
        transition(id, actor, ActionType.CLOSE, ChangeStatus.CLOSED, "关闭")
    }

    suspend fun reopen(id: String, actor: String, reason: String = "") {
        val note = if (reason.isBlank()) "重开" else "重开：$reason"
        transition(id, actor, ActionType.REOPEN, ChangeStatus.REOPENED, note)
    }

    private suspend fun transition(
        id: String,
        actor: String,
        action: ActionType,
        toStatus: ChangeStatus,
        note: String
    ) {
        val old = changeDao.getById(id) ?: return
        val updated = old.copy(
            status = toStatus,
            updatedAt = System.currentTimeMillis()
        )
        changeDao.upsert(updated)
        appendLog(id, action, actor, note)
    }

    private suspend fun appendLog(changeId: String, action: ActionType, actor: String, note: String) {
        val now = System.currentTimeMillis()
        val log = ChangeLogEntity(
            id = UUID.randomUUID().toString(),
            changeId = changeId,
            action = action,
            actor = actor,
            at = now,
            note = note
        )
        logDao.insert(log)
    }

    private fun ChangeEntity.toModel(): ChangeItem =
        ChangeItem(
            id = id,
            creator = creator,
            createdAt = createdAt,
            updatedAt = updatedAt,
            status = status,
            type = type,
            title = title,
            content = content,
            urgent = urgent,
            line = line,
            equipment = equipment,
            process = process
        )

    private fun ChangeLogEntity.toModel(): ChangeLog =
        ChangeLog(
            id = id,
            changeId = changeId,
            action = action,
            actor = actor,
            at = at,
            note = note
        )
}