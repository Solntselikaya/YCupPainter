package com.velikanova.ycuppainter.ui.screen.project

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent
import com.velikanova.ycuppainter.ui.screen.project.mvi.ProjectIntent.*
import com.velikanova.ycuppainter.ui.screen.project.painter.DrawMode
import com.velikanova.ycuppainter.ui.screen.project.painter.PainterMotionEvent
import com.velikanova.ycuppainter.ui.screen.project.painter.PathProps
import com.velikanova.ycuppainter.ui.theme.Green
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
                undoAvailable = state.haveChangesToUndo,
                redoAvailable = state.haveChangesToRedo,
                onUndoClick = { viewModel.reduce(Undo) },
                onRedoClick = { viewModel.reduce(Redo) },
                onDeleteClick = { /*TODO*/ },
                onAddFrameClick = { /*TODO*/ },
                onLayersClick = { /*TODO*/ },
                onPauseClick = { /*TODO*/ },
                onPlayClick = { /*TODO*/ }
            )
        },
        content = { paddingValues ->
            Content(
                paddingValues = paddingValues,
                drawColor = state.color,
                drawStrokeWidth = state.strokeWidth,
                drawMode = state.drawMode,
                paths = state.paths,
                onDrawLine = { viewModel.reduce(DrawLine(it)) },
                onDrawEraserLine = { viewModel.reduce(DrawEraserLine(it)) },
            )
        },
        bottomBar = {
            BottomBar(
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
                enabled = undoAvailable
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_undo_arrow_24),
                    contentDescription = null,
                    tint = if (undoAvailable) MaterialTheme.colorScheme.onBackground else Color.Unspecified
                )
            }

            IconButton(
                onClick = onRedoClick,
                enabled = redoAvailable
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_redo_arrow_24),
                    contentDescription = null,
                    tint = if (redoAvailable) MaterialTheme.colorScheme.onBackground else Color.Unspecified
                )
            }
        }

        Row {
            IconButton(onClick = onDeleteClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_bin_32),
                    contentDescription = null
                )
            }

            IconButton(onClick = onAddFrameClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_frame_32),
                    contentDescription = null
                )
            }

            IconButton(onClick = onLayersClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_layers_32),
                    contentDescription = null
                )
            }
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = onPauseClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_stop_32),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }

            IconButton(onClick = onPlayClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_play_32),
                    contentDescription = null,
                    tint = Color.Unspecified
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
private fun BottomBar(
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
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_pen_32),
                    contentDescription = null,
                    tint = if (pressedButton == PEN) Green else MaterialTheme.colorScheme.onBackground
                )
            }
        )

        IconButton(
            onClick = {
                onBrushClick()
            },
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_brush_32),
                    contentDescription = null,
                    tint = if (pressedButton == BRUSH) Green else MaterialTheme.colorScheme.onBackground
                )
            }
        )

        IconButton(
            onClick = {
                onEraserClick()
            },
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_erase_32),
                    contentDescription = null,
                    tint = if (pressedButton == ERASER) Green else MaterialTheme.colorScheme.onBackground
                )
            }
        )

        IconButton(
            onClick = {
                onInstrumentsClick()
            },
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_instruments_32),
                    contentDescription = null,
                    tint = if (pressedButton == INSTRUMENTS) Green else MaterialTheme.colorScheme.onBackground
                )
            }
        )

        var colorsExpanded by remember { mutableStateOf(pressedButton == COLOR) }
        Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
            IconButton(
                onClick = {
                    colorsExpanded = true
                    onColorClick(selectedColorInt)
                }
            ) {
                val borderColor = if (pressedButton == COLOR) Green else Color.Transparent
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(1.5.dp, borderColor, CircleShape)
                        .size(28.dp)
                        .background(Color(selectedColorInt))
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