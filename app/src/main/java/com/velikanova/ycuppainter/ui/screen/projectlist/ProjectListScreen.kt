//package com.velikanova.ycuppainter.ui.screen.projects
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Divider
//import androidx.compose.material3.FabPosition
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Shapes
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.PreviewLightDark
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.compose.LocalLifecycleOwner
//import com.velikanova.ycuppainter.R
//import com.velikanova.ycuppainter.data.model.dto.ProjectShortDto
//import com.velikanova.ycuppainter.data.model.entity.ProjectEntity
//import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListEffect
//import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListEffect.*
//import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListIntent
//import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListIntent.*
//import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListState.Content
//import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListState.EmptyContent
//import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListState.Loading
//import com.velikanova.ycuppainter.ui.theme.PADDING_LARGE
//import com.velikanova.ycuppainter.ui.theme.PADDING_MEDIUM
//import com.velikanova.ycuppainter.ui.theme.YCupPainterTheme
//import com.velikanova.ycuppainter.utils.convertToReadableDateWithTime
//import java.time.ZonedDateTime
//import com.velikanova.ycuppainter.ui.screen.projects.additional.ProjectListDialogState as DialogState
//
//@Composable
//fun ProjectListScreen() {
//    val viewModel = ProjectListViewModel()
//    val state by viewModel.state.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.effect.collect { effect ->
//            when (effect) {
//                is NavigateToProject ->
//            }
//        }
//    }
//
//    Scaffold(
//        topBar = { TopBar() },
//        content = { paddingValues ->
//            when (state) {
//                Loading -> Loading()
//                EmptyContent -> EmptyContent()
//                is Content -> Contents(
//                    paddingValues = paddingValues,
//                    dialogState = (state as Content).dialogState,
//                    projects = (state as Content).projects,
//                    onProjectEditClick = { id -> viewModel.reduce(OnChooseProject(id)) },
//                    onProjectDeleteClick = { id -> viewModel.reduce(OnDeleteProjectClick(id)) },
//                    onDismissDialog = { viewModel.reduce(OnDismissDialog) },
//                    onAcceptDelete = { viewModel.reduce(OnDeleteProjectAccept) }
//                )
//            }
//        },
//        floatingActionButton = { Fab { viewModel.reduce(OnAddProjectClick) } },
//        floatingActionButtonPosition = FabPosition.End
//    )
//}
//
//@Composable
//private fun TopBar() {
//
//}
//
//@Composable
//private fun Loading() {
//    Box(
//        contentAlignment = Alignment.Center,
//        content = { CircularProgressIndicator() }
//    )
//}
//
//@Composable
//private fun EmptyContent() {
//    Box(
//        contentAlignment = Alignment.Center,
//        content = { Text(stringResource(R.string.empty_list)) }
//    )
//}
//
//@Composable
//private fun Contents(
//    paddingValues: PaddingValues,
//    dialogState: DialogState,
//    projects: List<ProjectShortDto>,
//    onProjectEditClick: (id: Int) -> Unit,
//    onProjectDeleteClick: (id: Int) -> Unit,
//    onDismissDialog: () -> Unit,
//    onAcceptDelete: () -> Unit,
//) {
//    when (dialogState) {
//        DialogState.HIDDEN -> Unit
//        DialogState.CONFIRM_DELETION -> TODO()
//        DialogState.CHANGE_NAME -> TODO()
//    }
//
//    LazyColumn(
//        modifier = Modifier
//            .padding(paddingValues)
//            .padding(horizontal = PADDING_LARGE, vertical = PADDING_MEDIUM)
//    ) {
//        items(
//            items = projects,
//            key = ProjectShortDto::id
//        ) {
//            ListItem(
//                name = it.name,
//                updated = it.updatedTs,
//                onDeleteClick = { onProjectDeleteClick(it.id) },
//                onEditClick = { onProjectEditClick(it.id) }
//            )
//        }
//    }
//}
//
//@Composable
//private fun ListItem(
//    name: String,
//    updated: ZonedDateTime,
//    onDeleteClick: () -> Unit,
//    onEditClick: () -> Unit
//) {
//    Column {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(PADDING_MEDIUM)
//        ) {
//            Column {
//                Text(
//                    text = name,
//                    fontWeight = FontWeight.Bold,
//                    style = MaterialTheme.typography.bodyLarge
//                )
//
//                Text(
//                    text = updated.convertToReadableDateWithTime(),
//                    color = MaterialTheme.colorScheme.onPrimary.copy(0.8f),
//                    fontWeight = FontWeight.Normal,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//
//            Icon(
//                modifier = Modifier.clickable { onDeleteClick() },
//                imageVector = Icons.Filled.Delete,
//                contentDescription = null
//            )
//
//            Icon(
//                modifier = Modifier.clickable { onEditClick() },
//                imageVector = Icons.Filled.Edit,
//                contentDescription = null
//            )
//        }
//
//        Divider(modifier = Modifier.padding(horizontal = PADDING_LARGE))
//    }
//}
//
//@Composable
//private fun Fab(onClick: () -> Unit) {
//    FloatingActionButton(
//        onClick = onClick,
//        shape = CircleShape,
//        content = {
//            Icon(
//                imageVector = Icons.Filled.Add,
//                contentDescription = stringResource(R.string.ic_descr_create_new_project)
//            )
//        }
//    )
//}
//
////@Composable
////private fun DeleteDialog() {
////    AlertDialog(
////        onDismissRequest = onDismissDialog,
////        title = {
////            Text(
////                text = stringResource(R.string.delete_project_dialog_title),
////                fontWeight = FontWeight.Bold,
////                style = MaterialTheme.typography.bodyLarge
////            )
////        },
////        text = {
////            Text(
////                text = stringResource(R.string.delete_project_dialog_body),
////                fontWeight = FontWeight.Normal,
////                style = MaterialTheme.typography.bodyMedium
////            )
////        },
////        confirmButton = {
////            Button(
////                onClick = onAcceptDelete,
////            ) {
////                Text(
////                    text = stringResource(R.string.yes),
////                    style = MaterialTheme.typography.labelLarge
////                )
////            }
////        },
////        dismissButton = {
////            Button(
////                onClick = onDismissDialog,
////                colors = ButtonDefaults.textButtonColors(
////                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
////                ),
////            ) {
////                Text(
////                    text = stringResource(R.string.no),
////                    style = MaterialTheme.typography.labelLarge
////                )
////            }
////        },
////        shape = RoundedCornerShape(PADDING_LARGE)
////    )
////}
//
//@PreviewLightDark
//@Composable
//private fun Preview() {
//    YCupPainterTheme {
//        ProjectListScreen()
//    }
//}