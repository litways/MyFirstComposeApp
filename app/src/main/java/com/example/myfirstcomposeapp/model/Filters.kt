package com.example.myfirstcomposeapp.model

data class Filters(
    val keyword: String = "",
    val urgentOnly: Boolean = false,
    val type: ChangeType? = null,
    val line: String = "",
    val equipment: String = "",
    val process: String = "",
    val statuses: Set<ChangeStatus> = emptySet()
)