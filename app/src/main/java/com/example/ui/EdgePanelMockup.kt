package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OneUiAmber
import com.example.ui.theme.OneUiBlue
import com.example.ui.theme.OneUiBlueContainerDark
import com.example.ui.theme.OneUiBlueContainerLight
import com.example.ui.theme.OneUiBlueLight
import com.example.ui.theme.OneUiDarkBackground
import com.example.ui.theme.OneUiDarkBorder
import com.example.ui.theme.OneUiDarkCard
import com.example.ui.theme.OneUiDarkCardPinned
import com.example.ui.theme.OneUiDarkSurface
import com.example.ui.theme.OneUiDarkTextPrimary
import com.example.ui.theme.OneUiDarkTextSecondary
import com.example.ui.theme.OneUiDarkTextTertiary
import com.example.ui.theme.OneUiGreen
import com.example.ui.theme.OneUiLightBackground
import com.example.ui.theme.OneUiLightBorder
import com.example.ui.theme.OneUiLightCard
import com.example.ui.theme.OneUiLightCardPinned
import com.example.ui.theme.OneUiLightSurface
import com.example.ui.theme.OneUiLightTextPrimary
import com.example.ui.theme.OneUiLightTextSecondary
import com.example.ui.theme.OneUiLightTextTertiary
import com.example.ui.theme.OneUiRed
import kotlinx.coroutines.delay

enum class ClipType(val label: String, val color: Color) {
  TEXT("TEXT", OneUiBlue),
  URL("LINK", OneUiGreen),
  CODE("CODE", OneUiAmber),
  NOTE("NOTE", OneUiBlueLight)
}

data class MockClipboardItem(
  val id: String,
  val content: String,
  val timestamp: String,
  val isPinned: Boolean = false,
  val type: ClipType = ClipType.TEXT
)

val initialMockClips = listOf(
  MockClipboardItem(
    id = "1",
    content = "Office Wi-Fi: Galaxy_OneUI_5G#2024!",
    timestamp = "Pinned",
    isPinned = true,
    type = ClipType.NOTE
  ),
  MockClipboardItem(
    id = "2",
    content = "https://developer.samsung.com/galaxy-edge/overview.html",
    timestamp = "Pinned",
    isPinned = true,
    type = ClipType.URL
  ),
  MockClipboardItem(
    id = "3",
    content = "OTP Verification Code: 938210",
    timestamp = "2 min ago",
    isPinned = false,
    type = ClipType.TEXT
  ),
  MockClipboardItem(
    id = "4",
    content = "Discuss ctrlSee Edge Panel architecture: Room DB, AccessibilityService auto-paste, and CocktailProvider integration.",
    timestamp = "18 min ago",
    isPinned = false,
    type = ClipType.NOTE
  ),
  MockClipboardItem(
    id = "5",
    content = "git commit -m 'feat(edge-panel): add clipboard capture service'",
    timestamp = "1 hour ago",
    isPinned = false,
    type = ClipType.CODE
  ),
  MockClipboardItem(
    id = "6",
    content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
    timestamp = "3 hours ago",
    isPinned = false,
    type = ClipType.TEXT
  ),
  MockClipboardItem(
    id = "7",
    content = "Package ID: ctrl.see.panel",
    timestamp = "Yesterday",
    isPinned = false,
    type = ClipType.TEXT
  )
)

@Composable
fun EdgePanelPrototypeScreen(
  isDarkTheme: Boolean,
  onToggleTheme: () -> Unit,
  viewModel: ClipboardViewModel = viewModel()
) {
  val clips by viewModel.clips.collectAsStateWithLifecycle()
  var isSelectionMode by remember { mutableStateOf(false) }
  val selectedIds = remember { mutableStateListOf<String>() }
  var searchQuery by remember { mutableStateOf("") }
  var isSearchVisible by remember { mutableStateOf(false) }

  // Target paste test input
  var targetAppText by remember { mutableStateOf("") }
  var pasteNotificationMessage by remember { mutableStateOf<String?>(null) }

  // Dialogs
  var showDeleteConfirmDialog by remember { mutableStateOf(false) }
  var showClearAllConfirmDialog by remember { mutableStateOf(false) }
  var pendingDeleteSingleItem by remember { mutableStateOf<MockClipboardItem?>(null) }
  var showSettingsSheet by remember { mutableStateOf(false) }

  // Presentation mode: "Overlay in Galaxy view" vs "Focused Panel"
  var isOverlayMode by remember { mutableStateOf(true) }

  // Simulate new copy input
  var newCopySimulationText by remember { mutableStateOf("") }

  // Auto-dismiss paste notification
  LaunchedEffect(pasteNotificationMessage) {
    if (pasteNotificationMessage != null) {
      delay(2400)
      pasteNotificationMessage = null
    }
  }

  // Duplicate handling / capture helper
  fun captureNewClip(text: String) {
    if (text.isBlank()) return
    viewModel.captureNewClip(text)
    pasteNotificationMessage = "Saved"
  }

  // Tap handler (copies to clipboard and pastes into active target)
  fun handleTapToPaste(item: MockClipboardItem) {
    viewModel.copyToSystemClipboard(item.content)
    targetAppText = if (targetAppText.isEmpty()) item.content else "$targetAppText\n${item.content}"
    pasteNotificationMessage = "Copied"
  }

  // Batch Pin/Unpin logic for selected items
  fun togglePinSelected() {
    if (selectedIds.isEmpty()) return
    val selectedClips = clips.filter { selectedIds.contains(it.id) }
    val allSelectedPinned = selectedClips.isNotEmpty() && selectedClips.all { it.isPinned }
    viewModel.togglePinSelected(selectedIds.toList())
    pasteNotificationMessage = if (!allSelectedPinned) "Pinned selected items" else "Unpinned selected items"
  }

  // Deletion logic
  fun deleteSelected() {
    viewModel.deleteSelected(selectedIds.toList())
    selectedIds.clear()
    isSelectionMode = false
    showDeleteConfirmDialog = false
    pasteNotificationMessage = "Deleted selected items"
  }

  fun clearHistory(preservePinned: Boolean = true) {
    viewModel.clearHistory(preservePinned)
    selectedIds.clear()
    isSelectionMode = false
    showClearAllConfirmDialog = false
    pasteNotificationMessage = if (preservePinned) "Cleared unpinned history" else "All clipboard items deleted"
  }

  // Filtering
  val filteredClips = remember(clips, searchQuery) {
    if (searchQuery.isBlank()) clips
    else clips.filter { it.content.contains(searchQuery, ignoreCase = true) }
  }

  val pinnedClips = filteredClips.filter { it.isPinned }
  val unpinnedClips = filteredClips.filter { !it.isPinned }

  // Root Layout
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(if (isDarkTheme) OneUiDarkBackground else OneUiLightBackground)
  ) {
    // Top Prototype Toolbar
    PrototypeControlBar(
      isDarkTheme = isDarkTheme,
      isOverlayMode = isOverlayMode,
      onToggleTheme = onToggleTheme,
      onToggleMode = { isOverlayMode = !isOverlayMode },
      onResetMock = {
        viewModel.restoreSampleData()
        selectedIds.clear()
        isSelectionMode = false
        targetAppText = ""
        pasteNotificationMessage = "Reset to default sample clips"
      },
      onToggleEmpty = {
        if (clips.isEmpty()) {
          viewModel.restoreSampleData()
          pasteNotificationMessage = "Loaded sample clips"
        } else {
          viewModel.clearHistory(preservePinned = false)
          pasteNotificationMessage = "Emptied list to test Empty State"
        }
      },
      isEmptyState = clips.isEmpty()
    )

    // Interactive Test Workspace
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
    ) {
      if (isOverlayMode) {
        // Simulated Samsung Phone Screen with Right-Side Edge Panel
        SimulatedPhoneWithEdgeOverlay(
          isDarkTheme = isDarkTheme,
          targetAppText = targetAppText,
          onTargetTextChanged = { targetAppText = it },
          newCopyText = newCopySimulationText,
          onNewCopyTextChanged = { newCopySimulationText = it },
          onSimulateCopy = {
            captureNewClip(newCopySimulationText)
            newCopySimulationText = ""
          },
          panelContent = {
            EdgePanelBody(
              isDarkTheme = isDarkTheme,
              pinnedClips = pinnedClips,
              unpinnedClips = unpinnedClips,
              isSelectionMode = isSelectionMode,
              selectedIds = selectedIds,
              searchQuery = searchQuery,
              isSearchVisible = isSearchVisible,
              onSearchQueryChange = { searchQuery = it },
              onToggleSearch = {
                isSearchVisible = !isSearchVisible
                if (!isSearchVisible) searchQuery = ""
              },
              onToggleSelectionMode = {
                isSelectionMode = !isSelectionMode
                if (!isSelectionMode) selectedIds.clear()
              },
              onSelectAll = {
                if (selectedIds.size == clips.size) selectedIds.clear()
                else {
                  selectedIds.clear()
                  selectedIds.addAll(clips.map { it.id })
                }
              },
              onItemClick = { item ->
                if (isSelectionMode) {
                  if (selectedIds.contains(item.id)) selectedIds.remove(item.id)
                  else selectedIds.add(item.id)
                } else {
                  handleTapToPaste(item)
                }
              },
              onItemLongClick = { item ->
                if (!isSelectionMode) {
                  isSelectionMode = true
                  selectedIds.add(item.id)
                }
              },
              onTogglePin = { item ->
                viewModel.togglePin(item)
                pasteNotificationMessage = if (!item.isPinned) "Pinned to top" else "Unpinned"
              },
              onTogglePinSelected = {
                togglePinSelected()
              },
              onDeleteSingle = { item ->
                pendingDeleteSingleItem = item
              },
              onDeleteSelected = {
                if (selectedIds.isNotEmpty()) {
                  showDeleteConfirmDialog = true
                }
              },
              onClearAll = {
                showClearAllConfirmDialog = true
              },
              onOpenSettings = {
                showSettingsSheet = true
              },
              onRestoreSamples = {
                viewModel.restoreSampleData()
                pasteNotificationMessage = "Restored sample clips"
              }
            )
          }
        )
      } else {
        // Focused Edge Panel View (maximizing the vertical narrow panel)
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          contentAlignment = Alignment.Center
        ) {
          Card(
            modifier = Modifier
              .widthIn(min = 280.dp, max = 360.dp)
              .fillMaxHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isDarkTheme) OneUiDarkSurface else OneUiLightSurface
            ),
            elevation = CardDefaults.cardElevation(8.dp),
            border = BorderStroke(1.dp, if (isDarkTheme) OneUiDarkBorder else OneUiLightBorder)
          ) {
            EdgePanelBody(
              isDarkTheme = isDarkTheme,
              pinnedClips = pinnedClips,
              unpinnedClips = unpinnedClips,
              isSelectionMode = isSelectionMode,
              selectedIds = selectedIds,
              searchQuery = searchQuery,
              isSearchVisible = isSearchVisible,
              onSearchQueryChange = { searchQuery = it },
              onToggleSearch = {
                isSearchVisible = !isSearchVisible
                if (!isSearchVisible) searchQuery = ""
              },
              onToggleSelectionMode = {
                isSelectionMode = !isSelectionMode
                if (!isSelectionMode) selectedIds.clear()
              },
              onSelectAll = {
                if (selectedIds.size == clips.size) selectedIds.clear()
                else {
                  selectedIds.clear()
                  selectedIds.addAll(clips.map { it.id })
                }
              },
              onItemClick = { item ->
                if (isSelectionMode) {
                  if (selectedIds.contains(item.id)) selectedIds.remove(item.id)
                  else selectedIds.add(item.id)
                } else {
                  handleTapToPaste(item)
                }
              },
              onItemLongClick = { item ->
                if (!isSelectionMode) {
                  isSelectionMode = true
                  selectedIds.add(item.id)
                }
              },
              onTogglePin = { item ->
                viewModel.togglePin(item)
                pasteNotificationMessage = if (!item.isPinned) "Pinned to top" else "Unpinned"
              },
              onTogglePinSelected = {
                togglePinSelected()
              },
              onDeleteSingle = { item ->
                pendingDeleteSingleItem = item
              },
              onDeleteSelected = {
                if (selectedIds.isNotEmpty()) {
                  showDeleteConfirmDialog = true
                }
              },
              onClearAll = {
                showClearAllConfirmDialog = true
              },
              onOpenSettings = {
                showSettingsSheet = true
              },
              onRestoreSamples = {
                viewModel.restoreSampleData()
                pasteNotificationMessage = "Restored sample clips"
              }
            )
          }
        }
      }

      // Samsung One UI Floating Toast Notification (Minimalist pill)
      if (pasteNotificationMessage != null) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = if (isDarkTheme) Color(0xF01E222A) else Color(0xF0242933),
          border = BorderStroke(1.dp, if (isDarkTheme) Color(0x2BFFFFFF) else Color(0x1A000000)),
          shadowElevation = 4.dp,
          modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 12.dp)
            .padding(horizontal = 24.dp)
            .testTag("oneui_toast_notification")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = Color(0xFFE2E8F0),
              modifier = Modifier.size(14.dp)
            )
            Text(
              text = pasteNotificationMessage ?: "",
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Normal
            )
          }
        }
      }
    }

    // Delete confirmation dialog
    if (showDeleteConfirmDialog) {
      AlertDialog(
        onDismissRequest = { showDeleteConfirmDialog = false },
        title = {
          Text(
            text = "Delete ${selectedIds.size} item${if (selectedIds.size > 1) "s" else ""}?",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        },
        text = {
          Text(
            text = "Selected clipboard entries will be permanently removed from ctrlSee history.",
            fontSize = 14.sp
          )
        },
        confirmButton = {
          Button(
            onClick = { deleteSelected() },
            colors = ButtonDefaults.buttonColors(containerColor = OneUiRed),
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("Delete", color = Color.White)
          }
        },
        dismissButton = {
          TextButton(
            onClick = { showDeleteConfirmDialog = false },
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("Cancel")
          }
        },
        shape = RoundedCornerShape(26.dp)
      )
    }

    // Single item delete confirmation
    pendingDeleteSingleItem?.let { item ->
      AlertDialog(
        onDismissRequest = { pendingDeleteSingleItem = null },
        title = {
          Text("Delete clipboard item?", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
          Text(
            text = "\"${item.content.take(45)}${if (item.content.length > 45) "..." else ""}\" will be removed.",
            fontSize = 14.sp
          )
        },
        confirmButton = {
          Button(
            onClick = {
              viewModel.deleteSingle(item)
              selectedIds.remove(item.id)
              pendingDeleteSingleItem = null
              pasteNotificationMessage = "Item deleted"
            },
            colors = ButtonDefaults.buttonColors(containerColor = OneUiRed),
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("Delete", color = Color.White)
          }
        },
        dismissButton = {
          TextButton(
            onClick = { pendingDeleteSingleItem = null },
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("Cancel")
          }
        },
        shape = RoundedCornerShape(26.dp)
      )
    }

    // Clear All Confirmation Dialog
    if (showClearAllConfirmDialog) {
      AlertDialog(
        onDismissRequest = { showClearAllConfirmDialog = false },
        title = {
          Text("Clear clipboard history?", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
              "Choose whether to keep or remove pinned entries:",
              fontSize = 14.sp
            )
            Text(
              "• Pinned items (${clips.count { it.isPinned }}) will be saved if you keep them.",
              fontSize = 12.sp,
              color = OneUiBlue
            )
          }
        },
        confirmButton = {
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
              onClick = { clearHistory(preservePinned = true) },
              colors = ButtonDefaults.buttonColors(containerColor = OneUiBlue),
              shape = RoundedCornerShape(14.dp)
            ) {
              Text("Keep Pinned")
            }
            Button(
              onClick = { clearHistory(preservePinned = false) },
              colors = ButtonDefaults.buttonColors(containerColor = OneUiRed),
              shape = RoundedCornerShape(14.dp)
            ) {
              Text("Delete All")
            }
          }
        },
        dismissButton = {
          TextButton(
            onClick = { showClearAllConfirmDialog = false },
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("Cancel")
          }
        },
        shape = RoundedCornerShape(26.dp)
      )
    }

    // Settings / Architecture Info Modal
    if (showSettingsSheet) {
      AlertDialog(
        onDismissRequest = { showSettingsSheet = false },
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.Settings, contentDescription = null, tint = OneUiBlue)
            Text("ctrlSee Settings & Specs", fontWeight = FontWeight.Bold, fontSize = 18.sp)
          }
        },
        text = {
          Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 4.dp)
          ) {
            Text(
              "Samsung One UI 5 Edge Panel Integration",
              fontWeight = FontWeight.SemiBold,
              fontSize = 14.sp,
              color = OneUiBlue
            )
            Text(
              "• Application ID: ctrl.see.panel\n" +
                  "• Target OS: Android 13 / One UI 5\n" +
                  "• Min SDK: API 24 (Android 7.0 Nougat+)\n" +
                  "• Storage: Room local database (encrypted & private)\n" +
                  "• Background Monitor: AccessibilityService + focus capture\n" +
                  "• Tap-to-Paste: AccessibilityNodeInfo.ACTION_PASTE\n" +
                  "• Max History: 100 items (configurable)",
              fontSize = 13.sp,
              lineHeight = 18.sp
            )
            Surface(
              color = if (isDarkTheme) OneUiBlueContainerDark else OneUiBlueContainerLight,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                "Privacy Guarantee: Clipboard entries never leave the device. No network or tracking permissions requested.",
                modifier = Modifier.padding(10.dp),
                fontSize = 12.sp,
                color = if (isDarkTheme) OneUiBlueLight else OneUiBlue
              )
            }
          }
        },
        confirmButton = {
          Button(
            onClick = { showSettingsSheet = false },
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("Done")
          }
        },
        shape = RoundedCornerShape(26.dp)
      )
    }
  }
}

/**
 * Top control bar to test prototype configurations: theme, empty state, overlay mode, reset.
 */
@Composable
fun PrototypeControlBar(
  isDarkTheme: Boolean,
  isOverlayMode: Boolean,
  onToggleTheme: () -> Unit,
  onToggleMode: () -> Unit,
  onResetMock: () -> Unit,
  onToggleEmpty: () -> Unit,
  isEmptyState: Boolean
) {
  Surface(
    color = if (isDarkTheme) OneUiDarkSurface else OneUiLightSurface,
    shadowElevation = 2.dp,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // App title
        Text(
          text = "ctrlSee",
          color = if (isDarkTheme) OneUiDarkTextPrimary else OneUiLightTextPrimary,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          modifier = Modifier.padding(start = 2.dp)
        )
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // Mode toggle: Overlay vs Focused Panel
        IconButton(
          onClick = onToggleMode,
          modifier = Modifier.size(36.dp).testTag("toggle_view_mode")
        ) {
          Icon(
            imageVector = if (isOverlayMode) Icons.Default.ContentPaste else Icons.Default.Send,
            contentDescription = "Toggle View Mode",
            tint = if (isDarkTheme) OneUiDarkTextSecondary else OneUiLightTextSecondary,
            modifier = Modifier.size(18.dp)
          )
        }

        // Empty state trigger
        IconButton(
          onClick = onToggleEmpty,
          modifier = Modifier.size(36.dp).testTag("toggle_empty_state")
        ) {
          Icon(
            imageVector = if (isEmptyState) Icons.Default.Refresh else Icons.Default.DeleteSweep,
            contentDescription = "Toggle Empty State",
            tint = if (isDarkTheme) OneUiDarkTextSecondary else OneUiLightTextSecondary,
            modifier = Modifier.size(18.dp)
          )
        }

        // Theme toggle
        IconButton(
          onClick = onToggleTheme,
          modifier = Modifier.size(36.dp).testTag("toggle_theme")
        ) {
          Icon(
            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
            contentDescription = "Toggle Theme",
            tint = if (isDarkTheme) OneUiDarkTextSecondary else OneUiLightTextSecondary,
            modifier = Modifier.size(18.dp)
          )
        }

        // Reset
        IconButton(
          onClick = onResetMock,
          modifier = Modifier.size(36.dp).testTag("reset_mock_data")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Reset mock data",
            tint = if (isDarkTheme) OneUiDarkTextSecondary else OneUiLightTextSecondary,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}

/**
 * Simulated Phone Screen with Samsung Galaxy Wallpaper, an active target text field,
 * and the right-side Edge Panel handle and slide-out ctrlSee panel.
 */
@Composable
fun SimulatedPhoneWithEdgeOverlay(
  isDarkTheme: Boolean,
  targetAppText: String,
  onTargetTextChanged: (String) -> Unit,
  newCopyText: String,
  onNewCopyTextChanged: (String) -> Unit,
  onSimulateCopy: () -> Unit,
  panelContent: @Composable () -> Unit
) {
  Box(modifier = Modifier.fillMaxSize()) {
    // Simulated Galaxy phone background (Messaging / Notes app where user is working)
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(end = 120.dp, top = 16.dp, start = 16.dp, bottom = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Simulated Active App Header
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isDarkTheme) OneUiDarkSurface.copy(alpha = 0.85f) else OneUiLightSurface.copy(alpha = 0.9f),
        border = BorderStroke(1.dp, if (isDarkTheme) OneUiDarkBorder else OneUiLightBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(OneUiGreen)
          )
          Text(
            text = "Active App: Samsung Notes / Messenger",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDarkTheme) OneUiDarkTextPrimary else OneUiLightTextPrimary
          )
        }
      }

      // Live Paste Target Area
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isDarkTheme) OneUiDarkCard.copy(alpha = 0.95f) else OneUiLightCard
        ),
        border = BorderStroke(1.dp, if (isDarkTheme) OneUiDarkBorder else OneUiLightBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Target Field",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = if (isDarkTheme) OneUiDarkTextSecondary else OneUiLightTextSecondary
            )
            if (targetAppText.isNotEmpty()) {
              TextButton(
                onClick = { onTargetTextChanged("") },
                modifier = Modifier.height(28.dp)
              ) {
                Text("Clear", fontSize = 11.sp, color = if (isDarkTheme) OneUiDarkTextTertiary else OneUiLightTextTertiary)
              }
            }
          }
          OutlinedTextField(
            value = targetAppText,
            onValueChange = onTargetTextChanged,
            placeholder = { Text("Active input field...", fontSize = 13.sp) },
            modifier = Modifier
              .fillMaxWidth()
              .height(90.dp)
              .testTag("target_paste_field"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = if (isDarkTheme) OneUiDarkTextSecondary else OneUiLightTextSecondary,
              unfocusedBorderColor = if (isDarkTheme) OneUiDarkBorder else OneUiLightBorder
            ),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
          )
        }
      }

      // Simulate System Copy / Cut Action
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isDarkTheme) OneUiDarkCard.copy(alpha = 0.95f) else OneUiLightCard
        ),
        border = BorderStroke(1.dp, if (isDarkTheme) OneUiDarkBorder else OneUiLightBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "Copy Simulation",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDarkTheme) OneUiDarkTextSecondary else OneUiLightTextSecondary
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = newCopyText,
              onValueChange = onNewCopyTextChanged,
              placeholder = { Text("Copy text...", fontSize = 12.sp) },
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("simulate_copy_input"),
              shape = RoundedCornerShape(12.dp),
              singleLine = true,
              textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isDarkTheme) OneUiDarkTextSecondary else OneUiLightTextSecondary,
                unfocusedBorderColor = if (isDarkTheme) OneUiDarkBorder else OneUiLightBorder
              )
            )
            Button(
              onClick = onSimulateCopy,
              enabled = newCopyText.isNotBlank(),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isDarkTheme) Color(0xFF283244) else Color(0xFF334155)
              ),
              modifier = Modifier
                .height(48.dp)
                .testTag("simulate_copy_button")
            ) {
              Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Copy", fontSize = 12.sp)
            }
          }
        }
      }

      // Device Specs Card
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isDarkTheme) OneUiDarkSurface.copy(alpha = 0.5f) else OneUiLightSurface.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = "Samsung One UI 5 • Offline Local Storage",
          fontSize = 11.sp,
          color = if (isDarkTheme) OneUiDarkTextTertiary else OneUiLightTextTertiary,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
      }
    }

    // Samsung Edge Panel Handle Graphic on right edge
    Box(
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .padding(end = 268.dp) // Sits just outside the panel
        .size(width = 4.dp, height = 54.dp)
        .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
        .background(if (isDarkTheme) Color(0x66FFFFFF) else Color(0x4D000000))
    )

    // The Actual Samsung Edge Panel Overlay (Docked to Right Screen Edge)
    Surface(
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .width(268.dp)
        .fillMaxHeight()
        .shadow(16.dp, shape = RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp)),
      shape = RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp),
      color = if (isDarkTheme) OneUiDarkSurface else OneUiLightSurface,
      border = BorderStroke(1.dp, if (isDarkTheme) OneUiDarkBorder else OneUiLightBorder)
    ) {
      panelContent()
    }
  }
}

/**
 * The Edge Panel Body: Header, Search, Selection Bar, Pinned List, Recent List, Empty State.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EdgePanelBody(
  isDarkTheme: Boolean,
  pinnedClips: List<MockClipboardItem>,
  unpinnedClips: List<MockClipboardItem>,
  isSelectionMode: Boolean,
  selectedIds: List<String>,
  searchQuery: String,
  isSearchVisible: Boolean,
  onSearchQueryChange: (String) -> Unit,
  onToggleSearch: () -> Unit,
  onToggleSelectionMode: () -> Unit,
  onSelectAll: () -> Unit,
  onItemClick: (MockClipboardItem) -> Unit,
  onItemLongClick: (MockClipboardItem) -> Unit,
  onTogglePin: (MockClipboardItem) -> Unit,
  onTogglePinSelected: () -> Unit,
  onDeleteSingle: (MockClipboardItem) -> Unit,
  onDeleteSelected: () -> Unit,
  onClearAll: () -> Unit,
  onOpenSettings: () -> Unit,
  onRestoreSamples: () -> Unit
) {
  var showMenu by remember { mutableStateOf(false) }

  // Check if all selected items are currently pinned
  val selectedClips = remember(selectedIds, pinnedClips, unpinnedClips) {
    (pinnedClips + unpinnedClips).filter { selectedIds.contains(it.id) }
  }
  val allSelectedArePinned = selectedClips.isNotEmpty() && selectedClips.all { it.isPinned }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(if (isDarkTheme) OneUiDarkSurface else OneUiLightSurface)
  ) {
    // Edge Panel Drag Handle pill at the very top (Samsung One UI signature)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp, bottom = 4.dp),
      contentAlignment = Alignment.Center
    ) {
      Box(
        modifier = Modifier
          .size(width = 36.dp, height = 4.dp)
          .clip(RoundedCornerShape(2.dp))
          .background(if (isDarkTheme) OneUiDarkBorder else OneUiLightBorder)
      )
    }

    // Top Navigation / Header Bar
    if (isSelectionMode) {
      // Selection Mode Action Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          IconButton(
            onClick = onToggleSelectionMode,
            modifier = Modifier.size(36.dp).testTag("exit_selection_mode")
          ) {
            Icon(
              Icons.Default.Close,
              contentDescription = "Exit selection",
              tint = if (isDarkTheme) OneUiDarkTextPrimary else OneUiLightTextPrimary,
              modifier = Modifier.size(18.dp)
            )
          }
          Text(
            text = "${selectedIds.size} selected",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkTheme) OneUiDarkTextPrimary else OneUiLightTextPrimary
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
          TextButton(
            onClick = onSelectAll,
            modifier = Modifier.height(34.dp).testTag("select_all_button")
          ) {
            Text("All", fontSize = 12.sp, color = OneUiBlue)
          }

          // Pin / Unpin selected items button
          IconButton(
            onClick = onTogglePinSelected,
            enabled = selectedIds.isNotEmpty(),
            modifier = Modifier.size(36.dp).testTag("pin_selected_button")
          ) {
            Icon(
              imageVector = if (allSelectedArePinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
              contentDescription = if (allSelectedArePinned) "Unpin selected" else "Pin selected",
              tint = if (selectedIds.isNotEmpty()) {
                if (isDarkTheme) OneUiDarkTextPrimary else OneUiLightTextPrimary
              } else {
                if (isDarkTheme) OneUiDarkTextTertiary else OneUiLightTextTertiary
              },
              modifier = Modifier.size(18.dp)
            )
          }

          IconButton(
            onClick = onDeleteSelected,
            enabled = selectedIds.isNotEmpty(),
            modifier = Modifier.size(36.dp).testTag("delete_selected_button")
          ) {
            Icon(
              Icons.Default.Delete,
              contentDescription = "Delete selected",
              tint = if (selectedIds.isNotEmpty()) OneUiRed else (if (isDarkTheme) OneUiDarkTextTertiary else OneUiLightTextTertiary),
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    } else {
      // Normal One UI Edge Panel Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "ctrlSee",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = if (isDarkTheme) OneUiDarkTextPrimary else OneUiLightTextPrimary
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
          // Search icon
          IconButton(
            onClick = onToggleSearch,
            modifier = Modifier.size(32.dp).testTag("toggle_search_button")
          ) {
            Icon(
              Icons.Default.Search,
              contentDescription = "Search",
              tint = if (isDarkTheme) OneUiDarkTextSecondary else OneUiLightTextSecondary,
              modifier = Modifier.size(17.dp)
            )
          }

          // Options Menu
          Box {
            IconButton(
              onClick = { showMenu = true },
              modifier = Modifier.size(34.dp).testTag("edge_panel_menu")
            ) {
              Icon(
                Icons.Default.MoreVert,
                contentDescription = "Options",
                tint = if (isDarkTheme) OneUiDarkTextSecondary else OneUiLightTextSecondary,
                modifier = Modifier.size(18.dp)
              )
            }

            DropdownMenu(
              expanded = showMenu,
              onDismissRequest = { showMenu = false },
              modifier = Modifier.background(if (isDarkTheme) OneUiDarkCard else OneUiLightCard)
            ) {
              DropdownMenuItem(
                text = { Text("Select items", fontSize = 13.sp) },
                onClick = {
                  showMenu = false
                  onToggleSelectionMode()
                }
              )
              DropdownMenuItem(
                text = { Text("Clear history...", fontSize = 13.sp, color = OneUiRed) },
                onClick = {
                  showMenu = false
                  onClearAll()
                }
              )
              DropdownMenuItem(
                text = { Text("Settings & Privacy", fontSize = 13.sp) },
                onClick = {
                  showMenu = false
                  onOpenSettings()
                }
              )
            }
          }
        }
      }
    }

    // Search bar if opened
    AnimatedVisibility(visible = isSearchVisible) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 4.dp)
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchQueryChange,
          placeholder = { Text("Search clipboard...", fontSize = 12.sp) },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { onSearchQueryChange("") }, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(14.dp))
              }
            }
          },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("search_input_field"),
          singleLine = true,
          textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = OneUiBlue,
            unfocusedBorderColor = if (isDarkTheme) OneUiDarkBorder else OneUiLightBorder
          )
        )
      }
    }

    // Divider
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(if (isDarkTheme) OneUiDarkBorder else OneUiLightBorder)
    )

    // Content: List or Empty State
    if (pinnedClips.isEmpty() && unpinnedClips.isEmpty()) {
      // Empty State View
      EmptyStateView(
        isDarkTheme = isDarkTheme,
        searchActive = searchQuery.isNotBlank(),
        onRestore = onRestoreSamples
      )
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 10.dp)
          .testTag("clipboard_lazy_list"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Pinned Items Section
        if (pinnedClips.isNotEmpty()) {
          item {
            SectionHeader(
              title = "Pinned",
              count = pinnedClips.size,
              isDarkTheme = isDarkTheme
            )
          }
          items(pinnedClips, key = { it.id }) { clip ->
            ClipboardCard(
              clip = clip,
              isDarkTheme = isDarkTheme,
              isSelectionMode = isSelectionMode,
              isSelected = selectedIds.contains(clip.id),
              onClick = { onItemClick(clip) },
              onLongClick = { onItemLongClick(clip) },
              onTogglePin = { onTogglePin(clip) },
              onDelete = { onDeleteSingle(clip) }
            )
          }
          item {
            Spacer(modifier = Modifier.height(8.dp))
          }
        }

        // Recent Unpinned Section
        if (unpinnedClips.isNotEmpty()) {
          item {
            SectionHeader(
              title = if (pinnedClips.isNotEmpty()) "Recent" else "All",
              count = unpinnedClips.size,
              isDarkTheme = isDarkTheme
            )
          }
          items(unpinnedClips, key = { it.id }) { clip ->
            ClipboardCard(
              clip = clip,
              isDarkTheme = isDarkTheme,
              isSelectionMode = isSelectionMode,
              isSelected = selectedIds.contains(clip.id),
              onClick = { onItemClick(clip) },
              onLongClick = { onItemLongClick(clip) },
              onTogglePin = { onTogglePin(clip) },
              onDelete = { onDeleteSingle(clip) }
            )
          }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
      }
    }
  }
}

/**
 * Minimal Section Header
 */
@Composable
fun SectionHeader(
  title: String,
  count: Int,
  isDarkTheme: Boolean
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp, horizontal = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = title,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold,
      color = if (isDarkTheme) OneUiDarkTextSecondary else OneUiLightTextSecondary
    )
    Text(
      text = count.toString(),
      fontSize = 10.sp,
      color = if (isDarkTheme) OneUiDarkTextTertiary else OneUiLightTextTertiary
    )
  }
}

/**
 * Compact, touch-friendly One UI 5 card for individual clipboard items.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClipboardCard(
  clip: MockClipboardItem,
  isDarkTheme: Boolean,
  isSelectionMode: Boolean,
  isSelected: Boolean,
  onClick: () -> Unit,
  onLongClick: () -> Unit,
  onTogglePin: () -> Unit,
  onDelete: () -> Unit
) {
  val targetContainerColor = when {
    isSelected -> if (isDarkTheme) OneUiBlueContainerDark else OneUiBlueContainerLight
    clip.isPinned -> if (isDarkTheme) OneUiDarkCardPinned else OneUiLightCardPinned
    else -> if (isDarkTheme) OneUiDarkCard else OneUiLightCard
  }
  val containerColor by animateColorAsState(targetContainerColor, label = "card_color")

  val borderColor = when {
    isSelected -> OneUiBlue
    else -> if (isDarkTheme) OneUiDarkBorder else OneUiLightBorder
  }

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .combinedClickable(
        onClick = onClick,
        onLongClick = onLongClick
      )
      .testTag("clipboard_item_${clip.id}"),
    shape = RoundedCornerShape(12.dp),
    color = containerColor,
    border = BorderStroke(1.dp, borderColor)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
      // Top row: Colored square category indicator, timestamp, and actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          // Single colored rounded square for category (colors preserved from ClipType)
          Box(
            modifier = Modifier
              .size(7.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(clip.type.color)
          )

          // Timestamp
          Text(
            text = clip.timestamp,
            fontSize = 10.sp,
            color = if (isDarkTheme) OneUiDarkTextTertiary else OneUiLightTextTertiary
          )
        }

        // Action / State on right
        if (isSelectionMode) {
          Checkbox(
            checked = isSelected,
            onCheckedChange = { onClick() },
            colors = CheckboxDefaults.colors(
              checkedColor = OneUiBlue,
              uncheckedColor = if (isDarkTheme) OneUiDarkTextTertiary else OneUiLightTextTertiary
            ),
            modifier = Modifier.size(20.dp)
          )
        } else {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(0.dp)
          ) {
            // Pin/Unpin icon button
            IconButton(
              onClick = onTogglePin,
              modifier = Modifier.size(24.dp).testTag("pin_button_${clip.id}")
            ) {
              Icon(
                imageVector = if (clip.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                contentDescription = if (clip.isPinned) "Unpin" else "Pin",
                tint = if (clip.isPinned) (if (isDarkTheme) OneUiDarkTextSecondary else OneUiLightTextSecondary) else (if (isDarkTheme) OneUiDarkTextTertiary else OneUiLightTextTertiary),
                modifier = Modifier.size(13.dp)
              )
            }

            // Simple delete bin icon button
            IconButton(
              onClick = onDelete,
              modifier = Modifier.size(24.dp).testTag("delete_item_${clip.id}")
            ) {
              Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = if (isDarkTheme) OneUiDarkTextTertiary else OneUiLightTextTertiary,
                modifier = Modifier.size(13.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(5.dp))

      // Clipboard Content Preview
      Text(
        text = clip.content,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
        maxLines = if (clip.type == ClipType.CODE) 4 else 3,
        overflow = TextOverflow.Ellipsis,
        fontFamily = if (clip.type == ClipType.CODE) FontFamily.Monospace else FontFamily.Default,
        color = if (isDarkTheme) OneUiDarkTextPrimary else OneUiLightTextPrimary
      )
    }
  }
}

/**
 * Native One UI 5 Empty State View
 */
@Composable
fun EmptyStateView(
  isDarkTheme: Boolean,
  searchActive: Boolean,
  onRestore: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(20.dp)
      .testTag("empty_state_view"),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Surface(
      shape = CircleShape,
      color = if (isDarkTheme) Color(0xFF1E222A) else Color(0xFFEAEFF5),
      modifier = Modifier.size(56.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = if (searchActive) Icons.Default.Search else Icons.Default.ContentCopy,
          contentDescription = null,
          tint = if (isDarkTheme) OneUiDarkTextSecondary else OneUiLightTextSecondary,
          modifier = Modifier.size(24.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = if (searchActive) "No results" else "No clips",
      fontSize = 14.sp,
      fontWeight = FontWeight.SemiBold,
      color = if (isDarkTheme) OneUiDarkTextPrimary else OneUiLightTextPrimary
    )

    Spacer(modifier = Modifier.height(14.dp))

    Button(
      onClick = onRestore,
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = if (isDarkTheme) Color(0xFF283244) else Color(0xFF334155)
      ),
      modifier = Modifier.testTag("restore_samples_button")
    ) {
      Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
      Spacer(modifier = Modifier.width(6.dp))
      Text("Load Sample Clips", fontSize = 12.sp)
    }
  }
}
