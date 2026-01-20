package com.example.myfirstcomposeapp.model

enum class ChangeStatus {
    DRAFT,       // 草稿
    SUBMITTED,   // 已提交（待确认/待处理）
    CONFIRMED,   // 已确认
    RELEASED,    // 已放行/已发布
    CLOSED,      // 已关闭
    REJECTED,    // 已驳回
    REOPENED     // 已重开
}