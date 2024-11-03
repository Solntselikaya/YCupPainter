package com.velikanova.ycuppainter.ui.screen.projects

import androidx.lifecycle.viewModelScope
import com.velikanova.ycuppainter.architecture.MVIViewModel
import com.velikanova.ycuppainter.data.dao.ProjectDao
import com.velikanova.ycuppainter.data.model.entity.ProjectEntity
import com.velikanova.ycuppainter.ui.screen.projects.additional.ProjectListDialogState
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListIntent.*
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListState.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.UUID
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListState as State
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListIntent as Intent
import com.velikanova.ycuppainter.ui.screen.projects.mvi.ProjectListEffect as Effect

class ProjectListViewModel(
    private val projectDao: ProjectDao
): MVIViewModel<State, Intent, Effect>() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(Loading)
    override val state: StateFlow<State> = _state

    private val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
    override val effect: SharedFlow<Effect> = _effect

    init {
        viewModelScope.launch(Dispatchers.IO) {
            projectDao
                .selectAllAsFlow()
                .onEach { projects ->
                    _state.update {
                        Content(
                            projects = projects,
                            dialogState = ProjectListDialogState.HIDDEN
                        )
                    }
                }
                .collect()
        }
    }

    override fun reduce(intent: Intent) {
        when (intent) {
            OnAddProjectClick -> createProject()
            is OnChooseProject -> onEditProject(intent.id)
            is OnCreateProject -> TODO()
            OnDeleteProjectAccept -> TODO()
            is OnDeleteProjectClick -> TODO()
            OnDismissDialog -> TODO()
        }
    }

    private fun createProject() = viewModelScope.launch {
        projectDao.upsert(
            ProjectEntity(
                id = 0,
                name = UUID.randomUUID().toString(),
                lastUsedColorInt = null,
                lastUsedStrokeWidth = null,
                updatedTs = ZonedDateTime.now()
            )
        )
    }

    private fun onEditProject(id: Int) = viewModelScope.launch {
//        _effect.emit()
    }
}