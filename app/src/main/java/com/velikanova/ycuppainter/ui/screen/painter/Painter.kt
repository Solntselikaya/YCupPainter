package com.velikanova.ycuppainter.ui.screen.painter

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.velikanova.ycuppainter.R
import com.velikanova.ycuppainter.ui.screen.painter.BottomBarButton.*
import com.velikanova.ycuppainter.ui.theme.Green
import com.velikanova.ycuppainter.ui.theme.PADDING_LARGE
import com.velikanova.ycuppainter.ui.theme.PADDING_MEDIUM
import com.velikanova.ycuppainter.ui.theme.PADDING_SMALL
import com.velikanova.ycuppainter.ui.theme.YCupPainterTheme

@Composable
fun Painter() {


    Scaffold(
        topBar = {
            TopBar(
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
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAddFrameClick: () -> Unit,
    onLayersClick: () -> Unit,
    onPauseClick: () -> Unit,
    onPlayClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(PADDING_LARGE),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            IconButton(onClick = onUndoClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_undo_arrow_24),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(PADDING_MEDIUM))

            IconButton(onClick = onRedoClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_redo_arrow_24),
                    contentDescription = null
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

            Spacer(modifier = Modifier.width(PADDING_LARGE))
            IconButton(onClick = onAddFrameClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_frame_32),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(PADDING_LARGE))
            IconButton(onClick = onLayersClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_layers_32),
                    contentDescription = null
                )
            }
        }

        Row {
            IconButton(onClick = onPauseClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_stop_32),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(PADDING_LARGE))

            IconButton(onClick = onPlayClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_play_32),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun Content(
    paddingValues: PaddingValues
) {
    val background = ImageBitmap.imageResource(R.drawable.canvas_background)

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .padding(PADDING_LARGE)
    ) {
        Canvas(
            modifier = Modifier,
        ) {
            drawImage(
                image = background
            )
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
            .padding(top = PADDING_SMALL),
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
                    tint = if (pressed[PEN] == true) MaterialTheme.colorScheme.onBackground else Green
                )
            }
        )

        Spacer(modifier = Modifier.width(PADDING_LARGE))

        IconButton(
            onClick = {
                onBrushClick()
                pressButton(BRUSH)
            },
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_brush_32),
                    contentDescription = null,
                    tint = if (pressed[BRUSH] == true) MaterialTheme.colorScheme.onBackground else Green
                )
            }
        )

        Spacer(modifier = Modifier.width(PADDING_LARGE))

        IconButton(
            onClick = {
                onEraserClick()
                pressButton(ERASER)
            },
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_erase_32),
                    contentDescription = null,
                    tint = if (pressed[ERASER] == true) MaterialTheme.colorScheme.onBackground else Green
                )
            }
        )

        Spacer(modifier = Modifier.width(PADDING_LARGE))

        IconButton(
            onClick = {
                onInstrumentsClick()
                pressButton(INSTRUMENTS)
            },
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_instruments_32),
                    contentDescription = null,
                    tint = if (pressed[INSTRUMENTS] == true) MaterialTheme.colorScheme.onBackground else Green
                )
            }
        )

        Spacer(modifier = Modifier.width(PADDING_LARGE))

        IconButton(
            onClick = {
                onColorClick()
                pressButton(COLOR)
            }
        ) {
            val borderColor = if (pressed[COLOR] == true) Color.Transparent else Green

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(1.5.dp, borderColor, CircleShape)
                    .size(28.dp)
                    .background(Color(selectedColorInt))
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    YCupPainterTheme {
        Painter()
    }
}