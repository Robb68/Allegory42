package com.realm444.core.model

import kotlinx.datetime.Instant

/** Adjacency-list graph node — a full graph model, not a flat-string shortcut. */
enum class NodeType {
    WHAT,
    WHERE,
    WHY,
    WHEN,
    CHOICE_HABIT,
    FEELING,
    FREEFORM
}

/**
 * One branch of a marker's mind map. Shallow graph, not a document: MVP
 * renders these as a flat expandable card, but the adjacency-list structure
 * underneath means a future node-graph UI is a rendering change, not a data
 * migration (Section 4).
 */
data class MindMapNode(
    val id: String,
    val markerId: String,
    /** Null means attached directly to the marker (root-level node). */
    val parentNodeId: String? = null,
    val nodeType: NodeType,
    val content: String,
    /** Angle offset for rendering only — cosmetic, never semantic. */
    val positionHint: Double? = null,
    val createdAt: Instant,
    val editedAt: Instant,
)
