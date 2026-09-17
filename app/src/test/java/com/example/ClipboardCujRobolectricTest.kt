package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.ClipDao
import com.example.data.ClipItemEntity
import com.example.data.ClipboardRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ClipboardCujRobolectricTest {

  private lateinit var db: AppDatabase
  private lateinit var dao: ClipDao
  private lateinit var repository: ClipboardRepository

  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    dao = db.clipDao()
    repository = ClipboardRepository(dao)
  }

  @After
  fun closeDb() {
    db.close()
  }

  @Test
  fun testInsertAndRetrieveClips() = runBlocking {
    val item1 = ClipItemEntity(
      id = "1",
      content = "https://example.com/oneui",
      timestampText = "10:00 AM",
      createdAt = 1000L,
      isPinned = false,
      typeName = "URL"
    )
    val item2 = ClipItemEntity(
      id = "2",
      content = "Meeting notes at 3pm",
      timestampText = "10:05 AM",
      createdAt = 2000L,
      isPinned = true,
      typeName = "TEXT"
    )

    repository.insertOrUpdate(item1)
    repository.insertOrUpdate(item2)

    val allClips = repository.allClips.first()
    assertEquals(2, allClips.size)
    // Pinned should be first
    assertEquals("2", allClips[0].id)
    assertTrue(allClips[0].isPinned)
  }

  @Test
  fun testPinToggle() = runBlocking {
    val item = ClipItemEntity(
      id = "item-1",
      content = "Draft text",
      timestampText = "Just now",
      createdAt = 500L,
      isPinned = false
    )
    repository.insertOrUpdate(item)

    repository.updatePinStatus("item-1", true)
    var all = repository.allClips.first()
    assertTrue(all.find { it.id == "item-1" }?.isPinned == true)

    repository.updatePinStatus("item-1", false)
    all = repository.allClips.first()
    assertFalse(all.find { it.id == "item-1" }?.isPinned == true)
  }

  @Test
  fun testSingleAndBatchDeletion() = runBlocking {
    val items = listOf(
      ClipItemEntity(id = "a", content = "A", timestampText = "1m ago", createdAt = 10L),
      ClipItemEntity(id = "b", content = "B", timestampText = "2m ago", createdAt = 20L),
      ClipItemEntity(id = "c", content = "C", timestampText = "3m ago", createdAt = 30L, isPinned = true)
    )
    repository.insertAll(items)
    assertEquals(3, repository.allClips.first().size)

    // Delete single
    repository.deleteById("a")
    assertEquals(2, repository.allClips.first().size)

    // Clear unpinned clips only
    repository.clearUnpinned()
    val remaining = repository.allClips.first()
    assertEquals(1, remaining.size)
    assertEquals("c", remaining[0].id)
    assertTrue(remaining[0].isPinned)
  }

  @Test
  fun testBatchDeleteByIds() = runBlocking {
    val items = listOf(
      ClipItemEntity(id = "1", content = "One", timestampText = "1m", createdAt = 10L),
      ClipItemEntity(id = "2", content = "Two", timestampText = "2m", createdAt = 20L),
      ClipItemEntity(id = "3", content = "Three", timestampText = "3m", createdAt = 30L)
    )
    repository.insertAll(items)
    assertEquals(3, repository.allClips.first().size)

    repository.deleteByIds(listOf("1", "3"))
    val remaining = repository.allClips.first()
    assertEquals(1, remaining.size)
    assertEquals("2", remaining[0].id)
  }
}
