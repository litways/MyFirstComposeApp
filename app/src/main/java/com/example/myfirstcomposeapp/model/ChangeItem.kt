package com.example.myfirstcomposeapp.model

data class ChangeItem(
    val id: String,
    val creator: String,
    val createdAt: Long,
    val updatedAt: Long,
    val status: ChangeStatus = ChangeStatus.DRAFT,
    val type: ChangeType,
    val title: String,
    val content: String,
    val urgent: Boolean,
    val line: String,
    val equipment: String,
    val process: String
)