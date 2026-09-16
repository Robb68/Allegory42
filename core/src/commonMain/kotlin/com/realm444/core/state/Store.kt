package com.realm444.core.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * The single unidirectional store (Manus Build Prompt Section 3). /androidApp
 * dispatches [Action]s in; [state] is the only way anything reads state back
 * out. No business logic lives outside [reduce] — /androidApp/interaction
 * only ever calls [dispatch].
 */
class Store(initialState: AppState = AppState()) {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<AppState> = _state.asStateFlow()

    fun dispatch(action: Action) {
        _state.value = reduce(_state.value, action)
    }
}
