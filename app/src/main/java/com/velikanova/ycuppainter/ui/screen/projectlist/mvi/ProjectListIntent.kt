package com.velikanova.ycuppainter.ui.screen.projectlist.mvi

import com.velikanova.ycuppainter.architecture.MVIIntent

sealed interface ProjectListIntent : MVIIntent {
    data class OnChooseProject(val id: Int) : ProjectListIntent

    data object OnAddProjectClick : ProjectListIntent
    data class OnCreateProject(val name: String) : ProjectListIntent

    data class OnDeleteProjectClick(val id: Int) : ProjectListIntent
    data object OnDeleteProjectAccept : ProjectListIntent

    data object OnDismissDialog : ProjectListIntent
}