package com.velikanova.ycuppainter.domain

import androidx.compose.ui.graphics.Path
import com.velikanova.ycuppainter.ui.screen.project.painter.PathProps

typealias Figure = Pair<Path, PathProps>
typealias Frame = List<Figure>

class FrameDrawing(
    private val initialFigures: List<Figure> = emptyList(),
    private val initialFrames: List<Frame> = emptyList()
) {
    private val figures = initialFigures.toMutableList()
    private val frames = initialFrames.toMutableList()

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

    fun getFigures(index: Int): List<Figure>? {
        return frames.getOrNull(index)?.toList()
    }

    fun getPrevFigures(): List<Figure> {
        return frames.lastOrNull() ?: emptyList()
    }

    fun getHaveMultipleFrames(): Boolean {
        return frames.isNotEmpty()
    }

    fun createFrame() {
        frames.add(figures.toList())
        figures.clear()
    }

    fun deleteFrame() {
        val prevFrameFigures = frames.removeLastOrNull() ?: emptyList()
        figures.clear()
        figures.addAll(prevFrameFigures)
    }
}