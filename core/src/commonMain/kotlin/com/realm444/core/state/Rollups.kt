package com.realm444.core.state

import com.realm444.core.engine.PatternEngine
import com.realm444.core.engine.PatternLink
import com.realm444.core.model.ChoiceVsHabit
import com.realm444.core.model.Emotion
import com.realm444.core.model.EmotionSignature
import com.realm444.core.model.Marker
import com.realm444.core.model.Section
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

/**
 * The selectable aggregation windows used by the patterns view — the 28-day
 * window from the original patterns view, plus the 7/365-day radar windows
 * added in v5 Section 7.3.
 */
enum class RollupWindow(val days: Int) {
    SEVEN_DAYS(7),
    TWENTY_EIGHT_DAYS(28),
    THREE_SIXTY_FIVE_DAYS(365),
}

/**
 * Pure aggregation functions over a marker list — the patterns view (choice-
 * vs-habit ratio, section rollups, top feeling tags, emotion radar,
 * cross-time surfacing) is a read model over these, never a separate store
 * of its own. All /core, testable without any UI.
 */
object Rollups {

    fun markersWithin(markers: List<Marker>, now: Instant, window: RollupWindow): List<Marker> {
        val cutoff = now - window.days.days
        return markers.filter { it.timestamp >= cutoff }
    }

    /** Proportion of markers in each [ChoiceVsHabit] bucket. Empty input yields all zeros, not NaN. */
    fun choiceVsHabitRatio(markers: List<Marker>): Map<ChoiceVsHabit, Double> {
        if (markers.isEmpty()) return ChoiceVsHabit.entries.associateWith { 0.0 }
        val counts = markers.groupingBy { it.choiceVsHabit }.eachCount()
        val total = markers.size.toDouble()
        return ChoiceVsHabit.entries.associateWith { (counts[it] ?: 0) / total }
    }

    /** Marker count per section — always contains all twelve keys, zero-filled. */
    fun sectionRollup(markers: List<Marker>): Map<Section, Int> {
        val counts = markers.groupingBy { it.sourceSection }.eachCount()
        return Section.entries.associateWith { counts[it] ?: 0 }
    }

    fun topFeelingTags(markers: List<Marker>, limit: Int = 5): List<Pair<String, Int>> =
        markers
            .asSequence()
            .flatMap { it.primaryFeelingTags }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { it.key to it.value }

    /**
     * Mean intensity per emotion across [markers] — the data behind the
     * radar/spider view (v5 Section 7.3). Callers pass an already
     * window-filtered list (see [markersWithin]).
     */
    fun emotionAggregate(markers: List<Marker>): EmotionSignature {
        if (markers.isEmpty()) return EmotionSignature.EMPTY
        val averages = Emotion.entries.associateWith { emotion ->
            markers.sumOf { it.emotionSignature[emotion] } / markers.size
        }
        return EmotionSignature(averages)
    }

    /**
     * "You've felt this way before" — the strongest pattern-sibling for each
     * of the most recent markers, for the cross-time surfacing list (v5
     * Section 7.4) and the in-scene constellation threads (v5 Section 5.3).
     * Runs against real stored data; returns nothing it can't back with an
     * actual [PatternLink].
     */
    fun crossTimeSurfacing(
        markers: List<Marker>,
        threshold: Double = PatternEngine.DEFAULT_THRESHOLD,
        limit: Int = 5,
    ): List<PatternLink> =
        markers
            .sortedByDescending { it.timestamp }
            .asSequence()
            .mapNotNull { marker -> PatternEngine.linksFor(marker, markers, threshold).firstOrNull() }
            .distinctBy { setOf(it.markerId, it.siblingMarkerId) }
            .take(limit)
            .toList()
}
