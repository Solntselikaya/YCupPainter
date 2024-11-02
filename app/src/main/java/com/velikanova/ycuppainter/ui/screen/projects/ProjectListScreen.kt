package com.velikanova.ycuppainter.ui.screen.projects

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.velikanova.ycuppainter.R
import com.velikanova.ycuppainter.data.model.ProjectEntity
import com.velikanova.ycuppainter.ui.screen.projects.additional.ProjectListDialogState as DialogState
import com.velikanova.ycuppainter.ui.screen.projects.additional.ProjectListScreenMode as ScreenMode
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListIntent.*
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListState.Content
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListState.EmptyContent
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListState.Loading
import com.velikanova.ycuppainter.ui.theme.YCupPainterTheme

@Composable
fun ProjectListScreen() {
    val viewModel = ProjectListViewModel()
    val state by viewModel.state.collectAsState()

    OnOpenScreen { viewModel.reduce(OnOpenScreen) }

    Scaffold(
        topBar = { TopBar() },
        content = { paddingValues ->
            when (state) {
                Loading -> Loading()
                EmptyContent -> EmptyContent()
                is Content -> Contents(
                    paddingValues = paddingValues,
                    dialogState = (state as Content).dialogState,
                    screenMode = (state as Content).screenMode,
                    projects = (state as Content).projects,
                    onProjectClick = { id -> viewModel.reduce(OnChooseProject(id)) }
                )
            }
        },
        floatingActionButton = { Fab { viewModel.reduce(OnAddProjectClick) } },
        floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
private fun TopBar() {

}

@Composable
private fun Loading() {
    Box(
        contentAlignment = Alignment.Center,
        content = { CircularProgressIndicator() }
    )
}

@Composable
private fun EmptyContent() {
    Box(
        contentAlignment = Alignment.Center,
        content = { Text(stringResource(R.string.empty_list)) }
    )
}

@Composable
private fun Contents(
    paddingValues: PaddingValues,
    dialogState: DialogState,
    screenMode: ScreenMode,
    projects: List<ProjectEntity>,
    onProjectClick: (id: Int) -> Unit,

    ) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
    ) {
        items(
            items = projects,
            key = ProjectEntity::id
        ) {

        }
    }
}

@Composable
private fun OnOpenScreen(onOpen: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.STARTED -> {
                onOpen()
            }

            else -> Unit
        }
    }
}

@Composable
private fun Fab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        content = {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.ic_descr_create_new_project)
            )
        }
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    YCupPainterTheme {
        ProjectListScreen()
    }
}