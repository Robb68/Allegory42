package com.realm444.core.state

import com.realm444.core.model.Marker
import com.realm444.core.model.MeaningStatement
import com.realm444.core.model.MindMapNode

/** Every way [AppState] can change. Dispatched by /androidApp/interaction — never mutated directly. */
sealed interface Action {
    /** A new marker logged from any of the twelve capture sections. Appended, preserving chronological order. */
    data class LogMarker(val marker: Marker) : Action

    /** Edit any field of an existing marker (title, tags, choice-vs-habit, emotion signature, anchor state). */
    data class EditMarker(val marker: Marker) : Action

    data class DeleteMarker(val markerId: String) : Action

    data class AddMindMapNode(val node: MindMapNode) : Action

    data class EditMindMapNode(val node: MindMapNode) : Action

    data class DeleteMindMapNode(val nodeId: String) : Action

    /** Captured once on first run; editable after (Section 5 pillar). */
    data class SetMeaningStatement(val statement: MeaningStatement) : Action
}
