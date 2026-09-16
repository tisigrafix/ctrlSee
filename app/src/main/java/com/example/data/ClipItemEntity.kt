package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clipboard_items")
data class ClipItemEntity(
  @PrimaryKey
  val id: String,
  val content: String,
  val timestampText: String,
  val createdAt: Long = System.currentTimeMillis(),
  val isPinned: Boolean = false,
  val typeName: String = "TEXT"
)
