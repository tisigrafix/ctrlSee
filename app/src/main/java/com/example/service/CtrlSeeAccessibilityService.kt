package com.example.service

import android.accessibilityservice.AccessibilityService
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Foundation Accessibility Service for Samsung One UI Edge Panel direct paste injection.
 * When enabled by user in Android Settings, this service allows:
 * 1. Identifying the currently focused editable input field across apps.
 * 2. Directly performing ACTION_PASTE on the active editable node.
 */
class CtrlSeeAccessibilityService : AccessibilityService() {

  companion object {
    var instance: CtrlSeeAccessibilityService? = null
      private set

    /**
     * Attempts to paste text into the currently focused edit text view.
     * Returns true if successfully pasted via accessibility node.
     */
    fun pasteIntoCurrentFocus(): Boolean {
      val service = instance ?: return false
      val root = service.rootInActiveWindow ?: return false
      val focusedNode = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
      return if (focusedNode != null && focusedNode.isEditable) {
        val result = focusedNode.performAction(AccessibilityNodeInfo.ACTION_PASTE)
        focusedNode.recycle()
        root.recycle()
        result
      } else {
        focusedNode?.recycle()
        root.recycle()
        false
      }
    }
  }

  override fun onServiceConnected() {
    super.onServiceConnected()
    instance = this
  }

  override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    // Window state or focus change tracking
  }

  override fun onInterrupt() {
    // Service interrupted
  }

  override fun onDestroy() {
    super.onDestroy()
    if (instance == this) {
      instance = null
    }
  }
}
