package com.velikanova.ycuppainter.ui.screen.project

import com.velikanova.ycuppainter.architecture.MVIViewModel
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton.*
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectEffect
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent.*
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectState
import com.velikanova.ycuppainter.ui.screen.project.painter.DrawMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectEffect as Effect
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent as Intent
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectState as State

class ProjectViewModel(

): MVIViewModel<State, Intent, Effect>() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    override val state: StateFlow<State> = _state

    private val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
    override val effect: SharedFlow<Effect> = _effect

    override fun reduce(intent: Intent) {
        when (intent) {
            is ChooseColor -> chooseColor(intent.color)
            ChooseEraser -> switchDrawMode(DrawMode.ERASE)
            ChoosePen -> switchDrawMode(DrawMode.DRAW)
            ChooseBrush -> chooseBrush()
            ChooseInstruments -> chooseInstruments()
        }
    }

    private fun chooseColor(color: Int) = _state.update { prevState ->
        prevState.copy(
            color = color,
            pressedBottomButton = COLOR
        )
    }

    private fun switchDrawMode(drawMode: DrawMode) = _state.update { prevState ->
        val pressed = if (drawMode == DrawMode.DRAW) PEN else ERASER
        prevState.copy(
            drawMode = drawMode,
            pressedBottomButton = pressed
        )
    }

    private fun chooseBrush() = _state.update { prevState ->
        prevState.copy(
            pressedBottomButton = BRUSH
        )
    }

    private fun chooseInstruments() = _state.update { prevState ->
        prevState.copy(
            pressedBottomButton = INSTRUMENTS
        )
    }
}