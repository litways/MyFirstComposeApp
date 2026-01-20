package com.example.myfirstcomposeapp.model

data class ChangeDraft(
    val title: String,          // 变化点（标题）
    val content: String,        // 变化内容
    val creator: String,        // 创建人
    val type: ChangeType,       // 变化点类型（非空：与 Room ChangeEntity 对齐）
    val urgent: Boolean,        // 是否紧急
    val line: String,           // 产线
    val equipment: String,      // 设备
    val process: String         // 工序
)