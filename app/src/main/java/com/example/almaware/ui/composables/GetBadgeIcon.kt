package com.example.almaware.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun getIconForType(type: String): ImageVector {
    return when(type) {
        "Timer" -> Icons.Filled.Timer
        "MultiCheckbox" -> Icons.Filled.CheckBox
        "Checkbox" -> Icons.Filled.CheckBox
        "Quizz" -> Icons.Filled.Quiz
        "QR-Code" -> Icons.Filled.QrCode2
        "Input" -> Icons.AutoMirrored.Filled.Input
        "DailyTracker" -> Icons.Filled.CalendarToday
        "Connection" -> Icons.Filled.Wifi
        "Documentation" -> Icons.Filled.Newspaper
        else -> Icons.Filled.QuestionMark
    }
}