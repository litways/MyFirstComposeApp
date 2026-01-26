package com.example.myfirstcomposeapp.model

enum class ChangeStatus(val label: String) {
    DRAFT("草稿"),
    SUBMITTED("已提交"),
    CONFIRMED("已确认"),
    RELEASED("已放行"),
    CLOSED("已关闭"),
    REJECTED("已驳回"),
    REOPENED("已重开")
}
