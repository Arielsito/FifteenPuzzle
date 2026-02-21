package com.example.fifteenpuzzle.presentation.modifiers

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Modifier.onSwipe(
    columns: Int,
    currentIndex: Int,
    lastIndex: Int,
    swap: (Int, Int) -> Unit,
): Modifier = composed {
    var hasMoved by remember { mutableStateOf(false) }

    var offsetX = 0f
    var offsetY = 0f

    this.pointerInput(currentIndex) {
        detectDragGestures(
            onDragStart = {
                offsetX = 0f
                offsetY = 0f
                hasMoved = false
            }
        ) { change, dragAmount ->
            change.consume()
            if (hasMoved) return@detectDragGestures

            offsetX += dragAmount.x
            offsetY += dragAmount.y
            val threshold = size.width / 4f

            val targetIndex = when {
                offsetX > threshold && (currentIndex + 1) % columns != 0 -> currentIndex + 1
                offsetX < -threshold && currentIndex % columns != 0 -> currentIndex - 1
                offsetY > threshold -> currentIndex + columns
                offsetY < -threshold -> currentIndex - columns
                else -> null
            }

            if (targetIndex != null && targetIndex in 0 .. lastIndex) {
                swap(currentIndex, targetIndex)
                hasMoved = true
            }
        }
    }
}