package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipDao {
  @Query("SELECT * FROM clipboard_items ORDER BY isPinned DESC, createdAt DESC")
  fun getAllClipsFlow(): Flow<List<ClipItemEntity>>

  @Query("SELECT * FROM clipboard_items ORDER BY isPinned DESC, createdAt DESC")
  suspend fun getAllClips(): List<ClipItemEntity>

  @Query("SELECT COUNT(*) FROM clipboard_items")
  suspend fun getCount(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdate(clip: ClipItemEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(clips: List<ClipItemEntity>)

  @Update
  suspend fun updateClip(clip: ClipItemEntity)

  @Query("UPDATE clipboard_items SET isPinned = :isPinned WHERE id = :id")
  suspend fun updatePinStatus(id: String, isPinned: Boolean)

  @Query("DELETE FROM clipboard_items WHERE id = :id")
  suspend fun deleteById(id: String)

  @Query("DELETE FROM clipboard_items WHERE id IN (:ids)")
  suspend fun deleteByIds(ids: List<String>)

  @Query("DELETE FROM clipboard_items WHERE isPinned = 0")
  suspend fun clearUnpinned()

  @Query("DELETE FROM clipboard_items")
  suspend fun clearAll()
}
