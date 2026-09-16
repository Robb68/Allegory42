package com.realm444.core.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

/**
 * One logged moment — one star in the cluster. A marker is a pointer into a
 * mind map, not a diary line (Section 5 pillar).
 *
 * [spiralPosition] is deliberately absent here: position is derived, never
 * hand-stored — it's a pure function of chronological order, computed by
 * [com.realm444.core.engine.SpiralEngine] from a marker's index within the
 * full ordered marker list.
 */
data class Marker(
    val id: String,
    val userId: String,
    val timestamp: Instant,
    val sourceSection: Section,
    val title: String,
    val primaryFeelingTags: List<String> = emptyList(),
    val choiceVsHabit: ChoiceVsHabit,
    val choiceVsHabitConfidence: Double,
    val isAnchor: Boolean = false,
    val targetDate: LocalDate? = null,
    val anchorStatus: AnchorStatus? = null,
    val emotionSignature: EmotionSignature = EmotionSignature.EMPTY,
) {
    init {
        require(choiceVsHabitConfidence in 0.0..1.0) {
            "choiceVsHabitConfidence must be in 0.0..1.0, was $choiceVsHabitConfidence"
        }
        require(isAnchor || (targetDate == null && anchorStatus == null)) {
            "targetDate/anchorStatus are only meaningful when isAnchor is true"
        }
    }
}
