package com.example.myfirstcomposeapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myfirstcomposeapp.model.ChangeStatus
import com.example.myfirstcomposeapp.model.ChangeType

@Entity(tableName = "change_points")
data class ChangeEntity(
    @PrimaryKey val id: String,
    val creator: String,
    val createdAt: Long,
    val updatedAt: Long,
    val status: ChangeStatus,
    val type: ChangeType,
    val title: String,
    val content: String,
    val urgent: Boolean,
    val line: String,
    val equipment: String,
    val process: String
)