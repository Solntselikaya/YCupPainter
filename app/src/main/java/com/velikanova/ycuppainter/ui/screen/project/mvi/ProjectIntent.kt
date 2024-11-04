package com.velikanova.ycuppainter.ui.screen.project.mvi

import androidx.compose.ui.graphics.Path
import androidx.core.util.Pools
import com.velikanova.ycuppainter.architecture.MVIIntent

sealed interface ProjectIntent: MVIIntent {
    data object ChoosePen : ProjectIntent
    data object ChooseEraser : ProjectIntent
    data class ChooseColor(val color: Int) : ProjectIntent

    data object ChooseBrush : ProjectIntent
    data object ChooseInstruments : ProjectIntent

    data class DrawLine(val path: Path) : ProjectIntent
    data class DrawEraserLine(val path: Path) : ProjectIntent

    data object Undo : ProjectIntent
    data object Redo : ProjectIntent

    data object AddFrame : ProjectIntent
    data object DeleteFrame : ProjectIntent

    data object Play : ProjectIntent
    data object Pause : ProjectIntent
}