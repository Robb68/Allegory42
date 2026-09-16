package com.realm444.core.state

/**
 * Pure state transition — the entire mutation surface of [AppState]. Kept
 * separate from [Store] so it's trivially unit-testable without touching
 * coroutines.
 */
fun reduce(state: AppState, action: Action): AppState = when (action) {
    is Action.LogMarker ->
        state.copy(markers = state.markers + action.marker)

    is Action.EditMarker ->
        state.copy(markers = state.markers.map { if (it.id == action.marker.id) action.marker else it })

    is Action.DeleteMarker ->
        state.copy(
            markers = state.markers.filterNot { it.id == action.markerId },
            mindMapNodes = state.mindMapNodes.filterNot { it.markerId == action.markerId },
        )

    is Action.AddMindMapNode ->
        state.copy(mindMapNodes = state.mindMapNodes + action.node)

    is Action.EditMindMapNode ->
        state.copy(mindMapNodes = state.mindMapNodes.map { if (it.id == action.node.id) action.node else it })

    is Action.DeleteMindMapNode ->
        state.copy(mindMapNodes = state.mindMapNodes.filterNot { it.id == action.nodeId })

    is Action.SetMeaningStatement ->
        state.copy(meaningStatement = action.statement)
}
