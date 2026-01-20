package com.example.myfirstcomposeapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChangeLogDao {

    @Query("SELECT * FROM change_logs WHERE changeId = :changeId ORDER BY at ASC")
    fun observeByChangeId(changeId: String): Flow<List<ChangeLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ChangeLogEntity)

    @Query("DELETE FROM change_logs WHERE changeId = :changeId")
    suspend fun deleteByChangeId(changeId: String)
}