package com.velikanova.ycuppainter.ui.screen.projects.mvi

import com.velikanova.ycuppainter.architecture.MVIIntent

sealed interface ProjectListIntent : MVIIntent {
    data object OnOpenScreen : ProjectListIntent
    data class OnChooseProject(val id: Int) : ProjectListIntent

    data object OnAddProjectClick : ProjectListIntent
    data class OnCreateProject(val name: String) : ProjectListIntent

    data class OnDeleteProjectClick(val id: Int) : ProjectListIntent
    data object OnDeleteProjectAccept : ProjectListIntent
}