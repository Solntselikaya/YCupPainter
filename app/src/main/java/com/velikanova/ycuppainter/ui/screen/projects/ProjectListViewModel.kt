package com.velikanova.ycuppainter.ui.screen.projects

import com.velikanova.ycuppainter.architecture.MVIViewModel
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListState
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListState.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListState as State
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListIntent as Intent
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListEffect as Effect

class ProjectListViewModel(

): MVIViewModel<State, Intent, Effect>() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(Loading)
    override val state: StateFlow<State> = _state

    private val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
    override val effect: SharedFlow<Effect> = _effect

    override fun reduce(intent: Intent) {
        when (intent) {

        }
    }
}