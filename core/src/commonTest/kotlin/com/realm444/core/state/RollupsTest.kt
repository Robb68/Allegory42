package com.realm444.core.state

import com.realm444.core.model.ChoiceVsHabit
import com.realm444.core.model.Emotion
import com.realm444.core.model.EmotionSignature
import com.realm444.core.model.Marker
import com.realm444.core.model.Section
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days

class RollupsTest {

    private fun marker(
        id: String,
        section: Section = Section.HONESTY,
        choiceVsHabit: ChoiceVsHabit = ChoiceVsHabit.CHOICE,
        tags: List<String> = emptyList(),
        emotions: EmotionSignature = EmotionSignature.EMPTY,
        timestamp: Instant = Instant.fromEpochMilliseconds(0),
    ) = Marker(
        id = id,
        userId = "u1",
        timestamp = timestamp,
        sourceSection = section,
        title = "title-$id",
        primaryFeelingTags = tags,
        choiceVsHabit = choiceVsHabit,
        choiceVsHabitConfidence = 0.9,
        emotionSignature = emotions,
    )

    @Test
    fun choiceVsHabitRatioOnEmptyListIsAllZeroNotNaN() {
        val ratio = Rollups.choiceVsHabitRatio(emptyList())
        ChoiceVsHabit.entries.forEach { assertEquals(0.0, ratio.getValue(it)) }
    }

    @Test
    fun choiceVsHabitRatioComputesProportions() {
        val markers = listOf(
            marker("1", choiceVsHabit = ChoiceVsHabit.CHOICE),
            marker("2", choiceVsHabit = ChoiceVsHabit.CHOICE),
            marker("3", choiceVsHabit = ChoiceVsHabit.HABIT),
            marker("4", choiceVsHabit = ChoiceVsHabit.UNCLEAR),
        )
        val ratio = Rollups.choiceVsHabitRatio(markers)
        assertEquals(0.5, ratio.getValue(ChoiceVsHabit.CHOICE))
        assertEquals(0.25, ratio.getValue(ChoiceVsHabit.HABIT))
        assertEquals(0.25, ratio.getValue(ChoiceVsHabit.UNCLEAR))
    }

    @Test
    fun sectionRollupAlwaysReturnsAllTwelveSections() {
        val rollup = Rollups.sectionRollup(listOf(marker("1", section = Section.SERVICE)))
        assertEquals(Section.entries.size, rollup.size)
        assertEquals(1, rollup.getValue(Section.SERVICE))
        assertEquals(0, rollup.getValue(Section.HOPE))
    }

    @Test
    fun topFeelingTagsSortsByFrequencyDescending() {
        val markers = listOf(
            marker("1", tags = listOf("grateful", "tired")),
            marker("2", tags = listOf("grateful")),
            marker("3", tags = listOf("anxious")),
        )
        val top = Rollups.topFeelingTags(markers, limit = 2)
        // ties break by first-seen order: "tired" (marker 1) precedes "anxious" (marker 3)
        assertEquals(listOf("grateful" to 2, "tired" to 1), top)
    }

    @Test
    fun emotionAggregateAveragesEachDimensionIndependently() {
        val markers = listOf(
            marker("1", emotions = EmotionSignature.of(Emotion.JOY to 1.0, Emotion.ANXIETY to 0.2)),
            marker("2", emotions = EmotionSignature.of(Emotion.JOY to 0.0, Emotion.ANXIETY to 0.8)),
        )
        val aggregate = Rollups.emotionAggregate(markers)
        assertEquals(0.5, aggregate[Emotion.JOY])
        assertEquals(0.5, aggregate[Emotion.ANXIETY])
        assertEquals(0.0, aggregate[Emotion.ANGER])
    }

    @Test
    fun emotionAggregateOnEmptyListIsEmptySignature() {
        assertEquals(EmotionSignature.EMPTY, Rollups.emotionAggregate(emptyList()))
    }

    @Test
    fun markersWithinFiltersByWindowRelativeToNow() {
        val now = Instant.fromEpochMilliseconds(0)
        val inWindow = marker("recent", timestamp = now - 3.days)
        val outOfWindow = marker("stale", timestamp = now - 40.days)

        val within28 = Rollups.markersWithin(listOf(inWindow, outOfWindow), now, RollupWindow.TWENTY_EIGHT_DAYS)

        assertEquals(listOf("recent"), within28.map { it.id })
    }

    @Test
    fun crossTimeSurfacingOnlyReturnsRealPatternLinks() {
        val signature = EmotionSignature.of(Emotion.SADNESS to 0.9)
        val today = marker("today", section = Section.HOPE, emotions = signature, timestamp = Instant.fromEpochMilliseconds(1_000))
        val monthsAgo = marker("months-ago", section = Section.HOPE, emotions = signature, timestamp = Instant.fromEpochMilliseconds(0))
        val unrelated = marker(
            "unrelated",
            section = Section.JUSTICE,
            choiceVsHabit = ChoiceVsHabit.HABIT,
            emotions = EmotionSignature.of(Emotion.ANGER to 1.0),
            timestamp = Instant.fromEpochMilliseconds(500),
        )

        val highlights = Rollups.crossTimeSurfacing(listOf(today, monthsAgo, unrelated))

        assertTrue(highlights.isNotEmpty())
        assertTrue(highlights.all { setOf(it.markerId, it.siblingMarkerId) == setOf("today", "months-ago") })
    }
}
