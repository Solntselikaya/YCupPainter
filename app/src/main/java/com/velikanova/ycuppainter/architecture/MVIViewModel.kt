package com.velikanova.ycuppainter.architecture

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class MVIViewModel<S: MVIState, I: MVIIntent, E: MVIEffect>: ViewModel() {
    abstract val state: StateFlow<S>
    abstract val effect: SharedFlow<E>
    abstract fun reduce(intent: I)
}