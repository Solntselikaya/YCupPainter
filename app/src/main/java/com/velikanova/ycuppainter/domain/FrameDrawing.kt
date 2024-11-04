package com.velikanova.ycuppainter.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.toArgb
import com.velikanova.ycuppainter.ui.screen.project.painter.PathProps

typealias Figure = Pair<Path, PathProps>

class FrameDrawing(
    private val initialFigures: List<Figure>,
) {
    private val figures = initialFigures.toMutableList()

    fun drawLine(figure: Figure) {
        figures.add(figure)
    }

    fun removeLine(path: Path) {
        if (figures.isEmpty()) return

        figures.removeIf { it.first == path }
    }

    fun getFigures(): List<Figure> {
        return figures.toList()
    }
}