package com.realm444.core.state

import com.realm444.core.model.ChoiceVsHabit
import com.realm444.core.model.Marker
import com.realm444.core.model.MeaningStatement
import com.realm444.core.model.MindMapNode
import com.realm444.core.model.NodeType
import com.realm444.core.model.Section
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ReducerTest {

    private fun marker(id: String) = Marker(
        id = id,
        userId = "u1",
        timestamp = Instant.fromEpochMilliseconds(0),
        sourceSection = Section.HONESTY,
        title = "t",
        choiceVsHabit = ChoiceVsHabit.CHOICE,
        choiceVsHabitConfidence = 1.0,
    )

    @Test
    fun logMarkerAppendsPreservingOrder() {
        var state = AppState()
        state = reduce(state, Action.LogMarker(marker("1")))
        state = reduce(state, Action.LogMarker(marker("2")))
        assertEquals(listOf("1", "2"), state.markers.map { it.id })
    }

    @Test
    fun editMarkerReplacesInPlaceWithoutReordering() {
        var state = AppState()
        state = reduce(state, Action.LogMarker(marker("1")))
        state = reduce(state, Action.LogMarker(marker("2")))

        val edited = marker("1").copy(title = "edited")
        state = reduce(state, Action.EditMarker(edited))

        assertEquals(listOf("1", "2"), state.markers.map { it.id })
        assertEquals("edited", state.markerById("1")?.title)
    }

    @Test
    fun deleteMarkerCascadesToItsMindMapNodes() {
        var state = AppState()
        state = reduce(state, Action.LogMarker(marker("1")))
        val node = MindMapNode(
            id = "n1", markerId = "1", nodeType = NodeType.WHAT, content = "c",
            createdAt = Instant.fromEpochMilliseconds(0), editedAt = Instant.fromEpochMilliseconds(0),
        )
        state = reduce(state, Action.AddMindMapNode(node))

        state = reduce(state, Action.DeleteMarker("1"))

        assertTrue(state.markers.isEmpty())
        assertTrue(state.mindMapNodes.isEmpty())
    }

    @Test
    fun setMeaningStatementReplacesExisting() {
        var state = AppState()
        val first = MeaningStatement("m1", "u1", "why", Instant.fromEpochMilliseconds(0), Instant.fromEpochMilliseconds(0))
        val second = first.copy(text = "revised", editedAt = Instant.fromEpochMilliseconds(1))

        state = reduce(state, Action.SetMeaningStatement(first))
        state = reduce(state, Action.SetMeaningStatement(second))

        assertEquals("revised", state.meaningStatement?.text)
    }

    @Test
    fun storeDispatchAppliesReducer() {
        val store = Store()
        store.dispatch(Action.LogMarker(marker("1")))
        assertEquals(1, store.state.value.markers.size)
        assertNull(store.state.value.meaningStatement)
    }
}
