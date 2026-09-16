package com.realm444.core.state

import com.realm444.core.model.Marker
import com.realm444.core.model.MeaningStatement
import com.realm444.core.model.MindMapNode

/**
 * The single source of truth for the app. Markers are kept in chronological
 * order (oldest first) — that order is what [com.realm444.core.engine.SpiralEngine]
 * uses to derive each marker's position; it is never reordered for display.
 */
data class AppState(
    val markers: List<Marker> = emptyList(),
    val mindMapNodes: List<MindMapNode> = emptyList(),
    val meaningStatement: MeaningStatement? = null,
) {
    fun nodesFor(markerId: String): List<MindMapNode> = mindMapNodes.filter { it.markerId == markerId }

    fun markerById(markerId: String): Marker? = markers.find { it.id == markerId }
}
