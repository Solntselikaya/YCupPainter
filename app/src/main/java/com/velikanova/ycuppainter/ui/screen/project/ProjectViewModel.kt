package com.velikanova.ycuppainter.ui.screen.project

import androidx.compose.ui.graphics.Path
import androidx.lifecycle.viewModelScope
import com.velikanova.ycuppainter.architecture.MVIViewModel
import com.velikanova.ycuppainter.domain.AnimationPainterInvoker
import com.velikanova.ycuppainter.domain.FrameDrawing
import com.velikanova.ycuppainter.domain.command.CreateFrameCommand
import com.velikanova.ycuppainter.domain.command.DeleteFrameCommand
import com.velikanova.ycuppainter.domain.command.DrawEraserLineCommand
import com.velikanova.ycuppainter.domain.command.DrawLineCommand
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton.BRUSH
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton.COLOR
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton.ERASER
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton.INSTRUMENTS
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton.PEN
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent.ChooseBrush
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent.ChooseColor
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent.ChooseEraser
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent.ChooseInstruments
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent.ChoosePen
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent.DrawEraserLine
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent.DrawLine
import com.velikanova.ycuppainter.ui.screen.project.painter.DrawMode
import com.velikanova.ycuppainter.ui.screen.project.painter.PathProps
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectEffect as Effect
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent as Intent
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectState as State

class ProjectViewModel(

): MVIViewModel<State, Intent, Effect>() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    override val state: StateFlow<State> = _state

    private val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
    override val effect: SharedFlow<Effect> = _effect

    private val painterInvoker = AnimationPainterInvoker()
    private var frameDrawing = FrameDrawing(initialFigures = emptyList())

    override fun reduce(intent: Intent) {
        when (intent) {
            is ChooseColor -> chooseColor(intent.color)
            ChooseEraser -> switchDrawMode(DrawMode.ERASE)
            ChoosePen -> switchDrawMode(DrawMode.DRAW)
            ChooseBrush -> chooseBrush()
            ChooseInstruments -> chooseInstruments()
            is DrawLine -> drawLine(intent.path)
            is DrawEraserLine -> drawEraserLine(intent.path)
            ProjectIntent.Undo -> undo()
            ProjectIntent.Redo -> redo()
            ProjectIntent.AddFrame -> addFrame()
            ProjectIntent.DeleteFrame -> deleteFrame()
            ProjectIntent.Play -> play()
            ProjectIntent.Pause -> pause()
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

    private fun drawLine(path: Path) {
        val props = PathProps(
            strokeWidth = _state.value.strokeWidth,
            color = _state.value.color,
            eraseMode = false
        )
        val command = DrawLineCommand(frameDrawing, path, props)
        painterInvoker.execute(command)
        updateStateByFrameDrawing()
    }

    private fun drawEraserLine(path: Path) {
        val props = PathProps(
            strokeWidth = _state.value.strokeWidth,
            color = _state.value.color,
            eraseMode = true
        )
        val command = DrawEraserLineCommand(frameDrawing, path, props)
        painterInvoker.execute(command)
        updateStateByFrameDrawing()
    }

    private fun undo() {
        painterInvoker.undo()
        updateStateByFrameDrawing()
    }

    private fun redo() {
        painterInvoker.redo()
        updateStateByFrameDrawing()
    }

    private fun addFrame() {
        val command = CreateFrameCommand(frameDrawing)
        painterInvoker.execute(command)
        updateStateByFrameDrawing()
    }

    private fun deleteFrame() {
        val command = DeleteFrameCommand(frameDrawing)
        painterInvoker.execute(command)
        updateStateByFrameDrawing()
    }

    private fun play() {
        _state.update { prevState ->
            prevState.copy(
                isPLaying = true,
                pressedBottomButton = null
            )
        }

        viewModelScope.launch {
            var index = 0
            while (_state.value.isPLaying) {
                val currPaths = frameDrawing.getFigures(index)

                _state.update { prevState ->
                    prevState.copy(
                        paths = currPaths ?: emptyList()
                    )
                }

                if (currPaths == null) index = 0 else index += 1
                delay(100L)
            }
        }
    }

    private fun pause() {
        _state.update { prevState ->
            prevState.copy(
                isPLaying = false,
                pressedBottomButton = PEN
            )
        }
        updateStateByFrameDrawing()
    }

    private fun updateStateByFrameDrawing() = _state.update { prevState ->
        prevState.copy(
            paths = frameDrawing.getFigures(),
            prevFramePaths = frameDrawing.getPrevFigures(),
            haveChangesToUndo = painterInvoker.getHaveCommandsToUndo(),
            haveChangesToRedo = painterInvoker.getHaveCommandsToRedo(),
            haveMultipleFrames = frameDrawing.getHaveMultipleFrames()
        )
    }
}