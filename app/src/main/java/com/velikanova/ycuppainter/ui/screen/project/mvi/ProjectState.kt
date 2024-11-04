package com.velikanova.ycuppainter.ui.screen.project.mvi

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.toArgb
import com.velikanova.ycuppainter.architecture.MVIState
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton
import com.velikanova.ycuppainter.ui.screen.project.painter.DrawMode
import com.velikanova.ycuppainter.ui.screen.project.painter.PathProps
import com.velikanova.ycuppainter.ui.theme.Blue

data class ProjectState(
    val drawMode: DrawMode = DrawMode.DRAW,
    val color: Int = Blue.toArgb(),
    val strokeWidth: Float = 10f,
    val pressedBottomButton: BottomBarButton? = null,
    val paths: List<Pair<Path, PathProps>> = emptyList(),
    val haveChangesToUndo: Boolean = false,
    val haveChangesToRedo: Boolean = false
): MVIState
