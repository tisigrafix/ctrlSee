package com.example.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ClipItemEntity
import com.example.data.ClipboardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClipboardViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: ClipboardRepository
  private val clipboardManager: ClipboardManager =
    application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

  val clips: StateFlow<List<MockClipboardItem>>

  init {
    val db = AppDatabase.getDatabase(application)
    repository = ClipboardRepository(db.clipDao())

    // Convert entities to MockClipboardItem domain objects
    clips = repository.allClips.map { entities ->
      entities.map { entity ->
        MockClipboardItem(
          id = entity.id,
          content = entity.content,
          timestamp = entity.timestampText,
          isPinned = entity.isPinned,
          type = try {
            ClipType.valueOf(entity.typeName)
          } catch (_: Exception) {
            ClipType.TEXT
          }
        )
      }
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

    // Pre-populate with initial starter clips if database is brand new/empty
    viewModelScope.launch {
      if (repository.getCount() == 0) {
        val initialEntities = initialMockClips.mapIndexed { index, item ->
          ClipItemEntity(
            id = item.id,
            content = item.content,
            timestampText = item.timestamp,
            createdAt = System.currentTimeMillis() - (index * 60_000L),
            isPinned = item.isPinned,
            typeName = item.type.name
          )
        }
        repository.insertAll(initialEntities)
      }
    }
  }

  // System clipboard capture
  fun captureFromSystemClipboard() {
    try {
      val primaryClip = clipboardManager.primaryClip
      if (primaryClip != null && primaryClip.itemCount > 0) {
        val text = primaryClip.getItemAt(0).text?.toString()
        if (!text.isNullOrBlank()) {
          captureNewClip(text)
        }
      }
    } catch (_: Exception) {}
  }

  // Add / Duplicate capture logic
  fun captureNewClip(text: String) {
    if (text.isBlank()) return
    viewModelScope.launch {
      val currentList = repository.getAll()
      val existing = currentList.find { it.content.trim() == text.trim() }

      val detectedType = when {
        text.startsWith("http://") || text.startsWith("https://") -> ClipType.URL
        text.contains("git ") || text.contains("fun ") || text.contains("val ") || text.contains("class ") -> ClipType.CODE
        else -> ClipType.TEXT
      }

      if (existing != null) {
        // Update createdAt so it sorts to the top
        repository.insertOrUpdate(
          existing.copy(
            createdAt = System.currentTimeMillis(),
            timestampText = if (existing.isPinned) "Pinned" else "Just now"
          )
        )
      } else {
        val newEntity = ClipItemEntity(
          id = System.currentTimeMillis().toString(),
          content = text.trim(),
          timestampText = "Just now",
          createdAt = System.currentTimeMillis(),
          isPinned = false,
          typeName = detectedType.name
        )
        repository.insertOrUpdate(newEntity)
      }
    }
  }

  // Toggle individual pin
  fun togglePin(item: MockClipboardItem) {
    viewModelScope.launch {
      val newPinState = !item.isPinned
      repository.updatePinStatus(item.id, newPinState)
    }
  }

  // Batch toggle pin for selected items
  fun togglePinSelected(selectedIds: List<String>) {
    if (selectedIds.isEmpty()) return
    viewModelScope.launch {
      val currentList = repository.getAll()
      val selectedClips = currentList.filter { selectedIds.contains(it.id) }
      val allSelectedArePinned = selectedClips.isNotEmpty() && selectedClips.all { it.isPinned }
      val targetPinned = !allSelectedArePinned

      selectedClips.forEach { clip ->
        repository.updatePinStatus(clip.id, targetPinned)
      }
    }
  }

  // Delete single item
  fun deleteSingle(item: MockClipboardItem) {
    viewModelScope.launch {
      repository.deleteById(item.id)
    }
  }

  // Batch delete selected items
  fun deleteSelected(selectedIds: List<String>) {
    if (selectedIds.isEmpty()) return
    viewModelScope.launch {
      repository.deleteByIds(selectedIds)
    }
  }

  // Clear history
  fun clearHistory(preservePinned: Boolean) {
    viewModelScope.launch {
      if (preservePinned) {
        repository.clearUnpinned()
      } else {
        repository.clearAll()
      }
    }
  }

  // Restore sample data
  fun restoreSampleData() {
    viewModelScope.launch {
      repository.clearAll()
      val initialEntities = initialMockClips.mapIndexed { index, item ->
        ClipItemEntity(
          id = (System.currentTimeMillis() + index).toString(),
          content = item.content,
          timestampText = item.timestamp,
          createdAt = System.currentTimeMillis() - (index * 60_000L),
          isPinned = item.isPinned,
          typeName = item.type.name
        )
      }
      repository.insertAll(initialEntities)
    }
  }

  // Tap to copy to system clipboard
  fun copyToSystemClipboard(content: String) {
    try {
      val clip = ClipData.newPlainText("ctrlSee", content)
      clipboardManager.setPrimaryClip(clip)
    } catch (_: Exception) {}
  }
}
