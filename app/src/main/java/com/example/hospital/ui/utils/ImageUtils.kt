package com.example.hospital.ui.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.*

/**
 * This function converts a ByteArray into a Bitmap and remembers it for performance.
 * It checks for null or empty byte arrays and handles errors during decoding.
 */

@Composable
fun rememberBitmapFromByteArray(byteArray: ByteArray?): Bitmap? {
    return remember(byteArray) {
        if (byteArray == null) {
            null
        } else if (byteArray.isEmpty()) {
            null
        } else {
            try {
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                bitmap
            } catch (e: Exception) {
                null
            }
        }
    }
}