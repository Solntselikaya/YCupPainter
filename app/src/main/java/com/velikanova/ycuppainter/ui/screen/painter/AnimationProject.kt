package com.velikanova.ycuppainter.ui.screen.painter

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.velikanova.ycuppainter.R
import com.velikanova.ycuppainter.ui.screen.painter.BottomBarButton.BRUSH
import com.velikanova.ycuppainter.ui.screen.painter.BottomBarButton.COLOR
import com.velikanova.ycuppainter.ui.screen.painter.BottomBarButton.ERASER
import com.velikanova.ycuppainter.ui.screen.painter.BottomBarButton.INSTRUMENTS
import com.velikanova.ycuppainter.ui.screen.painter.BottomBarButton.PEN
import com.velikanova.ycuppainter.ui.theme.Green
import com.velikanova.ycuppainter.ui.theme.PADDING_LARGE
import com.velikanova.ycuppainter.ui.theme.PADDING_MEDIUM
import com.velikanova.ycuppainter.ui.theme.YCupPainterTheme
import com.velikanova.ycuppainter.utils.handleEvents

private const val DELAY_AFTER_DOWN: Long = 27L

@Composable
fun AnimationProject() {
    Scaffold(
        topBar = {
            TopBar(
                undoAvailable = false,
                redoAvailable = false,
                onUndoClick = { /*TODO*/ },
                onRedoClick = { /*TODO*/ },
                onDeleteClick = { /*TODO*/ },
                onAddFrameClick = { /*TODO*/ },
                onLayersClick = { /*TODO*/ },
                onPauseClick = { /*TODO*/ },
                onPlayClick = { /*TODO*/ }
            )
        },
        content = { paddingValues ->
            Content(
                paddingValues = paddingValues
            )
        },
        bottomBar = {
            BottomBar(
                selectedColorInt = 0,
                onPenClick = { /*TODO*/ },
                onBrushClick = { /*TODO*/ },
                onEraserClick = { /*TODO*/ },
                onInstrumentsClick = { /*TODO*/ },
                onColorClick = { /*TODO*/ }
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
            IconButton(onClick = onUndoClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_undo_arrow_24),
                    contentDescription = null,
                    tint = if (undoAvailable) MaterialTheme.colorScheme.onBackground else Color.Unspecified
                )
            }

            IconButton(onClick = onRedoClick) {
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
    paddingValues: PaddingValues
) {
    val paths = remember { mutableStateListOf<Pair<Path, PathProps>>() }
    var currentPath by remember { mutableStateOf(Path()) }
    var drawMode by remember { mutableStateOf(DrawMode.DRAW) }

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
                    paths.add(Pair(currentPath, PathProps()))
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
                        drawPainterPath(path, Color.Black, 10f)
                    } else {
                        drawEraserPath(path, 10f)
                    }
                }

                if (motionEvent != PainterMotionEvent.IDLE) {
                    if (!false) {
                        drawPainterPath(currentPath, Color.Black, 10f)
                    } else {
                        drawEraserPath(currentPath, 10f)
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
    onPenClick: () -> Unit,
    onBrushClick: () -> Unit,
    onEraserClick: () -> Unit,
    onInstrumentsClick: () -> Unit,
    onColorClick: () -> Unit
) {
    val pressed = BottomBarButton.entries
        .associateWith { false }
        .toMutableMap()
    val pressButton: (BottomBarButton) -> Unit = {
        pressed.replaceAll { key, _ ->
            key == it
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.Center
    ) {

        IconButton(
            onClick = {
                onPenClick()
                pressButton(PEN)
            },
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_pen_32),
                    contentDescription = null,
                    tint = if (pressed[PEN] == true) Green else MaterialTheme.colorScheme.onBackground
                )
            }
        )

        IconButton(
            onClick = {
                onBrushClick()
                pressButton(BRUSH)
            },
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_brush_32),
                    contentDescription = null,
                    tint = if (pressed[BRUSH] == true) Green else MaterialTheme.colorScheme.onBackground
                )
            }
        )

        IconButton(
            onClick = {
                onEraserClick()
                pressButton(ERASER)
            },
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_erase_32),
                    contentDescription = null,
                    tint = if (pressed[ERASER] == true) Green else MaterialTheme.colorScheme.onBackground
                )
            }
        )

        IconButton(
            onClick = {
                onInstrumentsClick()
                pressButton(INSTRUMENTS)
            },
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_instruments_32),
                    contentDescription = null,
                    tint = if (pressed[INSTRUMENTS] == true) Green else MaterialTheme.colorScheme.onBackground
                )
            }
        )

        IconButton(
            onClick = {
                onColorClick()
                pressButton(COLOR)
            }
        ) {
            val borderColor = if (pressed[COLOR] == true) Green else Color.Transparent

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(1.5.dp, borderColor, CircleShape)
                    .size(28.dp)
                    .background(Color.Black)
            )
        }
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