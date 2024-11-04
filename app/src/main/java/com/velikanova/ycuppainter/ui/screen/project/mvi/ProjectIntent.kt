package com.velikanova.ycuppainter.ui.screen.project.mvi

import com.velikanova.ycuppainter.architecture.MVIIntent

sealed interface ProjectIntent: MVIIntent {
    data object ChoosePen : ProjectIntent
    data object ChooseEraser : ProjectIntent
    data class ChooseColor(val color: Int) : ProjectIntent

    data object ChooseBrush : ProjectIntent
    data object ChooseInstruments : ProjectIntent
}