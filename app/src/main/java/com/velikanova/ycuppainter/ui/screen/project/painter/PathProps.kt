package com.velikanova.ycuppainter.ui.screen.project.painter

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.velikanova.ycuppainter.ui.theme.Black

data class PathProps (
    var strokeWidth: Float = 10f,
    var color: Int = Black.toArgb(),
    var eraseMode: Boolean = false
)