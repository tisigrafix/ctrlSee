package com.example.data

import kotlinx.coroutines.flow.Flow

class ClipboardRepository(private val clipDao: ClipDao) {

  val allClips: Flow<List<ClipItemEntity>> = clipDao.getAllClipsFlow()

  suspend fun getCount(): Int = clipDao.getCount()

  suspend fun getAll(): List<ClipItemEntity> = clipDao.getAllClips()

  suspend fun insertOrUpdate(clip: ClipItemEntity) {
    clipDao.insertOrUpdate(clip)
  }

  suspend fun insertAll(clips: List<ClipItemEntity>) {
    clipDao.insertAll(clips)
  }

  suspend fun updatePinStatus(id: String, isPinned: Boolean) {
    clipDao.updatePinStatus(id, isPinned)
  }

  suspend fun deleteById(id: String) {
    clipDao.deleteById(id)
  }

  suspend fun deleteByIds(ids: List<String>) {
    clipDao.deleteByIds(ids)
  }

  suspend fun clearUnpinned() {
    clipDao.clearUnpinned()
  }

  suspend fun clearAll() {
    clipDao.clearAll()
  }
}
