package com.velikanova.ycuppainter.ui.screen.project.painter

import androidx.compose.ui.graphics.Color

data class PathProps (
    var strokeWidth: Float = 10f,
    var color: Color = Color.Black,
    var eraseMode: Boolean = false
)