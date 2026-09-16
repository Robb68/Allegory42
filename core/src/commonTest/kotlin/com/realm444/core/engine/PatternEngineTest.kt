package com.realm444.core.engine

import com.realm444.core.model.ChoiceVsHabit
import com.realm444.core.model.Emotion
import com.realm444.core.model.EmotionSignature
import com.realm444.core.model.Marker
import com.realm444.core.model.Section
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PatternEngineTest {

    private fun marker(
        id: String,
        section: Section = Section.HONESTY,
        choiceVsHabit: ChoiceVsHabit = ChoiceVsHabit.CHOICE,
        emotions: EmotionSignature = EmotionSignature.EMPTY,
    ) = Marker(
        id = id,
        userId = "u1",
        timestamp = Instant.fromEpochMilliseconds(0),
        sourceSection = section,
        title = "title-$id",
        choiceVsHabit = choiceVsHabit,
        choiceVsHabitConfidence = 0.9,
        emotionSignature = emotions,
    )

    @Test
    fun identicalEmotionVectorsAreMaximallySimilar() {
        val signature = EmotionSignature.of(Emotion.ANXIETY to 0.8, Emotion.SADNESS to 0.4)
        val a = marker("a", emotions = signature)
        val b = marker("b", emotions = signature)

        val links = PatternEngine.linksFor(a, listOf(a, b), threshold = 0.0)

        assertEquals(1, links.size, "must exclude the marker itself")
        assertEquals("b", links.single().siblingMarkerId)
        assertClose(1.0, links.single().emotionSimilarity)
    }

    @Test
    fun orthogonalEmotionsWithNoSharedTagsScoreLow() {
        val a = marker("a", section = Section.HONESTY, choiceVsHabit = ChoiceVsHabit.CHOICE,
            emotions = EmotionSignature.of(Emotion.JOY to 1.0))
        val b = marker("b", section = Section.JUSTICE, choiceVsHabit = ChoiceVsHabit.HABIT,
            emotions = EmotionSignature.of(Emotion.ANGER to 1.0))

        val links = PatternEngine.linksFor(a, listOf(b), threshold = 0.0)

        assertClose(0.0, links.single().score)
    }

    @Test
    fun sharedChoiceVsHabitAndSectionContributeEvenWithZeroEmotionSimilarity() {
        val a = marker("a", section = Section.COURAGE, choiceVsHabit = ChoiceVsHabit.HABIT,
            emotions = EmotionSignature.of(Emotion.JOY to 1.0))
        val b = marker("b", section = Section.COURAGE, choiceVsHabit = ChoiceVsHabit.HABIT,
            emotions = EmotionSignature.of(Emotion.ANGER to 1.0))

        val link = PatternEngine.linksFor(a, listOf(b), threshold = 0.0).single()

        assertTrue(link.sharedChoiceVsHabit)
        assertTrue(link.sharedSection)
        assertClose(0.40, link.score) // 0.25 + 0.15, zero emotion contribution
    }

    @Test
    fun unclearChoiceVsHabitNeverCountsAsShared() {
        val a = marker("a", choiceVsHabit = ChoiceVsHabit.UNCLEAR)
        val b = marker("b", choiceVsHabit = ChoiceVsHabit.UNCLEAR)

        val link = PatternEngine.linksFor(a, listOf(b), threshold = 0.0).single()

        assertTrue(!link.sharedChoiceVsHabit)
    }

    @Test
    fun thresholdFiltersWeakLinksAndResultsAreSortedByScoreDescending() {
        val strong = marker("strong", section = Section.LOVE, choiceVsHabit = ChoiceVsHabit.CHOICE,
            emotions = EmotionSignature.of(Emotion.HAPPINESS to 1.0))
        val target = marker("target", section = Section.LOVE, choiceVsHabit = ChoiceVsHabit.CHOICE,
            emotions = EmotionSignature.of(Emotion.HAPPINESS to 1.0))
        val weak = marker("weak", section = Section.JUSTICE, choiceVsHabit = ChoiceVsHabit.HABIT,
            emotions = EmotionSignature.of(Emotion.ANGER to 1.0))

        val links = PatternEngine.linksFor(target, listOf(strong, weak))

        assertEquals(listOf("strong"), links.map { it.siblingMarkerId })
    }
}

private fun assertClose(expected: Double, actual: Double, epsilon: Double = 1e-9) {
    assertTrue(kotlin.math.abs(expected - actual) < epsilon, "expected $expected, was $actual")
}
