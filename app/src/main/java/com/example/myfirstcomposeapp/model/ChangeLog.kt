package com.example.myfirstcomposeapp.model

data class ChangeLog(
    val id: String,
    val changeId: String,
    val action: ActionType,
    val actor: String,
    val at: Long,
    val note: String = ""
)