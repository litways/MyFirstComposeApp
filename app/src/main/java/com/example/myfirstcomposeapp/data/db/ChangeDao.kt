package com.example.myfirstcomposeapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfirstcomposeapp.model.ChangeStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ChangeDao {

    @Query("SELECT * FROM change_points ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<ChangeEntity>>

    @Query("SELECT * FROM change_points WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ChangeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ChangeEntity)

    @Query("DELETE FROM change_points WHERE id = :id")
    suspend fun deleteById(id: String)

    // 为后续态势/待办/追溯准备（今天先放进来）
    @Query("SELECT * FROM change_points WHERE status IN (:statuses) ORDER BY updatedAt DESC")
    fun observeByStatuses(statuses: List<ChangeStatus>): Flow<List<ChangeEntity>>
}