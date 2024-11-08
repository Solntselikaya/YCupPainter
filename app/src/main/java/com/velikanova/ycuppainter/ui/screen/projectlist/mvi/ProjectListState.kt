package com.velikanova.ycuppainter.ui.screen.projectlist.mvi

import com.velikanova.ycuppainter.architecture.MVIState
import com.velikanova.ycuppainter.data.model.dto.ProjectShortDto
import com.velikanova.ycuppainter.ui.screen.projectlist.additional.ProjectListDialogState as DialogState

sealed interface ProjectListState : MVIState {
    data object Loading : ProjectListState
    data object EmptyContent : ProjectListState
    data class Content(
        val projects: List<ProjectShortDto>,
        val dialogState: DialogState,
    ) : ProjectListState
}
