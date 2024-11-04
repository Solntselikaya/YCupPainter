package com.velikanova.ycuppainter.ui.screen.project

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.velikanova.ycuppainter.R
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton.BRUSH
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton.COLOR
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton.ERASER
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton.INSTRUMENTS
import com.velikanova.ycuppainter.ui.screen.project.additional.BottomBarButton.PEN
import com.velikanova.ycuppainter.ui.screen.project.additional.ColorPopup
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent.*
import com.velikanova.ycuppainter.ui.screen.project.painter.DrawMode
import com.velikanova.ycuppainter.ui.screen.project.painter.PainterMotionEvent
import com.velikanova.ycuppainter.ui.screen.project.painter.PathProps
import com.velikanova.ycuppainter.ui.theme.Green
import com.velikanova.ycuppainter.ui.theme.InactiveColor
import com.velikanova.ycuppainter.ui.theme.PADDING_LARGE
import com.velikanova.ycuppainter.ui.theme.PADDING_MEDIUM
import com.velikanova.ycuppainter.ui.theme.YCupPainterTheme
import com.velikanova.ycuppainter.ui.theme.defaultColorPalette
import com.velikanova.ycuppainter.utils.handleEvents

private const val DELAY_AFTER_DOWN: Long = 27L

@Composable
fun AnimationProject() {
    val viewModel = ProjectViewModel()
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                isPlaying = state.isPLaying,
                haveMultipleFrames = state.haveMultipleFrames,
                undoAvailable = state.haveChangesToUndo,
                redoAvailable = state.haveChangesToRedo,
                onUndoClick = { viewModel.reduce(Undo) },
                onRedoClick = { viewModel.reduce(Redo) },
                onDeleteClick = { viewModel.reduce(DeleteFrame) },
                onAddFrameClick = { viewModel.reduce(AddFrame) },
                onLayersClick = { /*TODO*/ },
                onPauseClick = { viewModel.reduce(Pause) },
                onPlayClick = { viewModel.reduce(Play) }
            )
        },
        content = { paddingValues ->
            if (state.isPLaying) {
                PlayAnimation(
                    paddingValues = paddingValues,
                    paths = state.paths
                )
            } else {
                Content(
                    paddingValues = paddingValues,
                    drawColor = state.color,
                    drawStrokeWidth = state.strokeWidth,
                    drawMode = state.drawMode,
                    paths = state.paths,
                    prevPaths = state.prevFramePaths,
                    onDrawLine = { viewModel.reduce(DrawLine(it)) },
                    onDrawEraserLine = { viewModel.reduce(DrawEraserLine(it)) },
                )
            }
        },
        bottomBar = {
            BottomBar(
                isPlaying = state.isPLaying,
                selectedColorInt = state.color,
                pressedButton = state.pressedBottomButton,
                onPenClick = { viewModel.reduce(ChoosePen) },
                onBrushClick = { viewModel.reduce(ChooseBrush) },
                onEraserClick = { viewModel.reduce(ChooseEraser) },
                onInstrumentsClick = { viewModel.reduce(ChooseInstruments) },
                onColorClick = { viewModel.reduce(ChooseColor(it)) }
            )
        }
    )
}

@Composable
private fun TopBar(
    isPlaying: Boolean,
    haveMultipleFrames: Boolean,
    undoAvailable: Boolean,
    redoAvailable: Boolean,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAddFrameClick: () -> Unit,
    onLayersClick: () -> Unit,
    onPauseClick: () -> Unit,
    onPlayClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PADDING_MEDIUM),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            IconButton(
                onClick = onUndoClick,
                enabled = (undoAvailable && !isPlaying)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_undo_arrow_24),
                    contentDescription = null,
                    tint = if (undoAvailable && !isPlaying) MaterialTheme.colorScheme.onBackground else Color.Unspecified
                )
            }

            IconButton(
                onClick = onRedoClick,
                enabled = (redoAvailable && !isPlaying)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_redo_arrow_24),
                    contentDescription = null,
                    tint = if (redoAvailable && !isPlaying) MaterialTheme.colorScheme.onBackground else Color.Unspecified
                )
            }
        }

        Row {
            IconButton(
                onClick = onDeleteClick,
                enabled = !isPlaying
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bin_32),
                    contentDescription = null,
                    tint = if (!isPlaying) MaterialTheme.colorScheme.onBackground else InactiveColor
                )
            }

            IconButton(
                onClick = onAddFrameClick,
                enabled = !isPlaying
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_frame_32),
                    contentDescription = null,
                    tint = if (!isPlaying) MaterialTheme.colorScheme.onBackground else InactiveColor
                )
            }

            IconButton(
                onClick = onLayersClick,
                enabled = !isPlaying
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_layers_32),
                    contentDescription = null,
                    tint = if (!isPlaying) MaterialTheme.colorScheme.onBackground else InactiveColor
                )
            }
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(
                onClick = onPauseClick,
                enabled = isPlaying
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_stop_32),
                    contentDescription = null,
                    tint = if (isPlaying) MaterialTheme.colorScheme.onBackground else Color.Unspecified
                )
            }

            IconButton(
                onClick = onPlayClick,
                enabled = (haveMultipleFrames && !isPlaying)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_play_32),
                    contentDescription = null,
                    tint = if (haveMultipleFrames && !isPlaying) MaterialTheme.colorScheme.onBackground else Color.Unspecified
                )
            }
        }
    }
}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    drawColor: Int,
    drawStrokeWidth: Float,
    drawMode: DrawMode,
    paths: List<Pair<Path, PathProps>>,
    prevPaths: List<Pair<Path, PathProps>>,
    onDrawLine: (Path) -> Unit,
    onDrawEraserLine: (Path) -> Unit
) {
    var currentPath by remember { mutableStateOf(Path()) }
    var motionEvent by remember { mutableStateOf(PainterMotionEvent.IDLE) }
    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
    var previousPosition by remember { mutableStateOf(Offset.Unspecified) }

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .padding(PADDING_LARGE)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.canvas_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        if (prevPaths.isNotEmpty()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                prevPaths.forEach {
                    val path = it.first
                    val property = it.second

                    if (!property.eraseMode) {
                        drawPainterPath(path, Color(property.color).copy(0.3f), property.strokeWidth)
                    } else {
                        drawEraserPath(path, property.strokeWidth)
                    }
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .handleEvents(
                    onDown = { pointerInputChange: PointerInputChange ->
                        currentPosition = pointerInputChange.position
                        motionEvent = PainterMotionEvent.DOWN
                        pointerInputChange.consume()
                    },
                    onMove = { pointerInputChange: PointerInputChange ->
                        motionEvent = PainterMotionEvent.MOVE
                        currentPosition = pointerInputChange.position
                        pointerInputChange.consume()
                    },
                    onUp = { pointerInputChange: PointerInputChange ->
                        motionEvent = PainterMotionEvent.UP
                        pointerInputChange.consume()
                    },
                    delayAfterDownInMillis = DELAY_AFTER_DOWN
                )
        ) {
            when (motionEvent) {
                PainterMotionEvent.DOWN -> {
                    currentPath.moveTo(currentPosition.x, currentPosition.y)
                    previousPosition = currentPosition
                }

                PainterMotionEvent.MOVE -> {
                    currentPath.quadraticTo(
                        previousPosition.x,
                        previousPosition.y,
                        (previousPosition.x + currentPosition.x) / 2,
                        (previousPosition.y + currentPosition.y) / 2
                    )
                    previousPosition = currentPosition
                }

                PainterMotionEvent.UP -> {
                    currentPath.lineTo(currentPosition.x, currentPosition.y)

                    if (drawMode == DrawMode.DRAW) {
                        onDrawLine(currentPath)
                    } else {
                        onDrawEraserLine(currentPath)
                    }

                    currentPath = Path()
                    currentPosition = Offset.Unspecified
                    previousPosition = currentPosition
                    motionEvent = PainterMotionEvent.IDLE
                }

                PainterMotionEvent.IDLE -> Unit
            }

            with(drawContext.canvas.nativeCanvas) {
                val checkPoint = saveLayer(null, null)

                paths.forEach {
                    val path = it.first
                    val property = it.second

                    if (!property.eraseMode) {
                        drawPainterPath(path, Color(property.color), property.strokeWidth)
                    } else {
                        drawEraserPath(path, property.strokeWidth)
                    }
                }

                if (motionEvent != PainterMotionEvent.IDLE) {
                    if (drawMode != DrawMode.ERASE) {
                        drawPainterPath(currentPath, Color(drawColor), drawStrokeWidth)
                    } else {
                        drawEraserPath(currentPath, drawStrokeWidth)
                    }
                }

                restoreToCount(checkPoint)
            }
        }
    }
}

@Composable
private fun PlayAnimation(
    paddingValues: PaddingValues,
    paths: List<Pair<Path, PathProps>>
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .padding(PADDING_LARGE)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.canvas_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            paths.forEach {
                val path = it.first
                val property = it.second

                if (!property.eraseMode) {
                    drawPainterPath(path, Color(property.color), property.strokeWidth)
                } else {
                    drawEraserPath(path, property.strokeWidth)
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    isPlaying: Boolean,
    selectedColorInt: Int,
    pressedButton: BottomBarButton?,
    onPenClick: () -> Unit,
    onBrushClick: () -> Unit,
    onEraserClick: () -> Unit,
    onInstrumentsClick: () -> Unit,
    onColorClick: (color: Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.Center
    ) {

        IconButton(
            onClick = {
                onPenClick()
            },
            enabled = !isPlaying,
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_pen_32),
                    contentDescription = null,
                    tint = if (pressedButton == PEN) Green else if (!isPlaying) MaterialTheme.colorScheme.onBackground else InactiveColor
                )
            }
        )

        IconButton(
            onClick = {
                onBrushClick()
            },
            enabled = !isPlaying,
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_brush_32),
                    contentDescription = null,
                    tint = if (pressedButton == BRUSH) Green else if (!isPlaying) MaterialTheme.colorScheme.onBackground else InactiveColor
                )
            }
        )

        IconButton(
            onClick = {
                onEraserClick()
            },
            enabled = !isPlaying,
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_erase_32),
                    contentDescription = null,
                    tint = if (pressedButton == ERASER) Green else if (!isPlaying) MaterialTheme.colorScheme.onBackground else InactiveColor
                )
            }
        )

        IconButton(
            onClick = {
                onInstrumentsClick()
            },
            enabled = !isPlaying,
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_instruments_32),
                    contentDescription = null,
                    tint = if (pressedButton == INSTRUMENTS) Green else if (!isPlaying) MaterialTheme.colorScheme.onBackground else InactiveColor
                )
            }
        )

        var colorsExpanded by remember { mutableStateOf(pressedButton == COLOR) }
        Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
            IconButton(
                onClick = {
                    colorsExpanded = true
                    onColorClick(selectedColorInt)
                },
                enabled = !isPlaying,
            ) {
                val selectedColor = Color(selectedColorInt)
                val borderColor = if (pressedButton == COLOR) {
                    Green
                } else if (selectedColor.toArgb() == MaterialTheme.colorScheme.background.toArgb()) {
                    MaterialTheme.colorScheme.onBackground
                } else {
                    Color.Transparent
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(1.5.dp, borderColor, CircleShape)
                        .size(28.dp)
                        .background(selectedColor)
                )
            }

            ColorPopup(
                expanded = colorsExpanded,
                onDismissRequest = {
                    colorsExpanded = false
                    onPenClick()
                }
            ) {
                MaterialTheme.colorScheme.defaultColorPalette.forEach {
                    ColorButton(it) { onColorClick(it.toArgb()) }
                }
            }
        }
    }
}

@Composable
private fun ColorButton(
    color: Color,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(28.dp)
                .background(color)
        )
    }
}

fun DrawScope.drawEraserPath(
    path: Path,
    width: Float
) {
    drawPath(
        color = Color.Transparent,
        path = path,
        style = Stroke(
            width = width,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        ),
        blendMode = BlendMode.Clear
    )
}

fun DrawScope.drawPainterPath(
    path: Path,
    color: Color,
    width: Float
) {
    drawPath(
        color = color,
        path = path,
        style = Stroke(
            width = width,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    YCupPainterTheme {
        AnimationProject()
    }
}