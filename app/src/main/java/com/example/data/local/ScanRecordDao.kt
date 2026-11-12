package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ScanRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanRecordDao {
    @Query("SELECT * FROM amulet_scans ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM amulet_scans WHERE id = :id LIMIT 1")
    suspend fun getScanById(id: String): ScanRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(record: ScanRecordEntity)

    @Update
    suspend fun updateScan(record: ScanRecordEntity)

    @Query("DELETE FROM amulet_scans WHERE id = :id")
    suspend fun deleteScanById(id: String)

    @Query("UPDATE amulet_scans SET isFavorite = :isFav WHERE id = :id")
    suspend fun toggleFavorite(id: String, isFav: Boolean)
}
