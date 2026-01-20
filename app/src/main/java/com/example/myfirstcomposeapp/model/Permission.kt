package com.example.myfirstcomposeapp.model

import com.example.myfirstcomposeapp.ui.viewmodel.AppRole

/**
 * Day9: 权限中心（角色 × 状态 × 动作）
 *
 * 说明：
 * - ActionType 使用你项目中已有的 model/ActionType（用于日志记录），如果你的 ActionType 命名不同，
 *   只需在下面的 ActionType 引用处对齐即可。
 */
object Permission {

    /**
     * 允许执行某动作？
     */
    fun can(role: AppRole, status: ChangeStatus, action: ActionType): Boolean {
        // 管理员：允许全流程操作（企业里通常具备兜底权限）
        if (role == AppRole.MANAGER) return when (action) {
            ActionType.SUBMIT,
            ActionType.CONFIRM,
            ActionType.REJECT,
            ActionType.RELEASE,
            ActionType.CLOSE,
            ActionType.REOPEN -> true
            else -> false
        }

        return when (action) {
            ActionType.SUBMIT -> when (role) {
                AppRole.CREATOR -> status == ChangeStatus.DRAFT || status == ChangeStatus.REOPENED
                else -> false
            }

            ActionType.CONFIRM -> when (role) {
                AppRole.QA -> status == ChangeStatus.SUBMITTED
                else -> false
            }

            ActionType.REJECT -> when (role) {
                AppRole.QA -> status == ChangeStatus.SUBMITTED || status == ChangeStatus.CONFIRMED
                else -> false
            }

            ActionType.RELEASE -> when (role) {
                AppRole.PROCESS -> status == ChangeStatus.CONFIRMED
                else -> false
            }

            ActionType.CLOSE -> when (role) {
                AppRole.CREATOR -> status == ChangeStatus.RELEASED
                else -> false
            }

            ActionType.REOPEN -> when (role) {
                AppRole.CREATOR -> status == ChangeStatus.CLOSED || status == ChangeStatus.REJECTED
                else -> false
            }

            else -> false
        }
    }

    /**
     * 返回某角色在某状态下可执行的动作集合（给 UI 做“显示/隐藏”）
     */
    fun allowedActions(role: AppRole, status: ChangeStatus): Set<ActionType> {
        val candidates = listOf(
            ActionType.SUBMIT,
            ActionType.CONFIRM,
            ActionType.REJECT,
            ActionType.RELEASE,
            ActionType.CLOSE,
            ActionType.REOPEN
        )
        return candidates.filter { can(role, status, it) }.toSet()
    }

    /**
     * 待办：对某角色来说“需要处理”的状态集合
     * 原则：该角色在该状态下至少有一个动作可做。
     */
    fun todoStatuses(role: AppRole): List<ChangeStatus> {
        val statuses = ChangeStatus.values().toList()
        return statuses.filter { s -> allowedActions(role, s).isNotEmpty() }
    }
}