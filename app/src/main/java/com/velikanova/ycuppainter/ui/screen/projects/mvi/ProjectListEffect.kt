package com.velikanova.ycuppainter.ui.screen.projects.mvi

import com.velikanova.ycuppainter.architecture.MVIEffect

sealed interface ProjectListEffect : MVIEffect {
    data class NavigateToProject(val id: Int) : ProjectListEffect
}