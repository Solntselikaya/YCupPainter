package com.velikanova.ycuppainter.ui.screen.painter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.velikanova.ycuppainter.R
import com.velikanova.ycuppainter.ui.theme.PADDING_LARGE
import com.velikanova.ycuppainter.ui.theme.PADDING_MEDIUM
import com.velikanova.ycuppainter.ui.theme.PADDING_SMALL
import com.velikanova.ycuppainter.ui.theme.YCupPainterTheme

@Composable
fun Painter() {
    Scaffold(
        topBar = {
            TopBar(
                onUndoClick = {},
                onRedoClick = {},
                onDeleteClick = {},
                onAddFrameClick = {},
                onLayersClick = {},
                onPauseClick = {},
                onPlayClick = {}
            )
        },
        content = { paddingValues ->
            Content(
                paddingValues = paddingValues
            )
        },
        bottomBar = {

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
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .padding(PADDING_LARGE)
    ) {

    }
}

@Composable
private fun BottomBar(
    onPenClick: () -> Unit,
    onBrushClick: () -> Unit,
    onEraserClick: () -> Unit,
    onInstrumentsClick: () -> Unit,
    onColorClick: () -> Unit
) {
    val pressedButton = remember {  }

    Row(
        modifier = Modifier
            .padding(top = PADDING_SMALL),
        horizontalArrangement = Arrangement.Absolute.Center
    ) {

        IconButton(onClick = onPenClick) {
            Icon(
                painter = painterResource(R.drawable.ic_pen_32),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(PADDING_LARGE))

        IconButton(onClick = onBrushClick) {
            Icon(
                painter = painterResource(R.drawable.ic_brush_32),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(PADDING_LARGE))

        IconButton(onClick = onEraserClick) {
            Icon(
                painter = painterResource(R.drawable.ic_erase_32),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(PADDING_LARGE))

        IconButton(onClick = onInstrumentsClick) {
            Icon(
                painter = painterResource(R.drawable.ic_instruments_32),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(PADDING_LARGE))

        IconButton(onClick = onColorClick) {
            Icon(
                painter = painterResource(R.drawable.c),
                contentDescription = null
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    YCupPainterTheme {

    }
}