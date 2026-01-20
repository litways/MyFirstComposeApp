package com.example.myfirstcomposeapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myfirstcomposeapp.model.ActionType

@Entity(tableName = "change_logs")
data class ChangeLogEntity(
    @PrimaryKey val id: String,
    val changeId: String,
    val action: ActionType,
    val actor: String,
    val at: Long,
    val note: String
)