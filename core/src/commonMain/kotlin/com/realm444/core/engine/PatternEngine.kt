package com.realm444.core.engine

import com.realm444.core.model.ChoiceVsHabit
import com.realm444.core.model.Marker
import kotlin.math.sqrt

/**
 * A computed relationship between two markers — never hand-authored, never
 * stored. Powers the constellation threads (v5 Section 5.3) and the
 * cross-time surfacing list (v5 Section 7.4).
 */
data class PatternLink(
    val markerId: String,
    val siblingMarkerId: String,
    /** Combined similarity in 0.0..1.0 — see [PatternEngine.SCORE_WEIGHTS]. */
    val score: Double,
    val emotionSimilarity: Double,
    val sharedChoiceVsHabit: Boolean,
    val sharedSection: Boolean,
)

/**
 * Computes pattern-siblings: markers that share a strong emotion-signature
 * and/or choice-vs-habit/section similarity with a given marker (v5 Section
 * 7.2). Pure and side-effect free — /core owns this computation; callers
 * (the state store) are responsible for caching results on read, since links
 * are derived and never persisted as a column.
 *
 * This is deliberately a real vector similarity computation, not a
 * same-single-tag check: [emotionSimilarity] is a cosine similarity across
 * the full seven-dimension emotion vector.
 */
object PatternEngine {

    /** Weight of emotion-vector similarity vs. shared choice/habit vs. shared section in [PatternLink.score]. */
    private const val EMOTION_WEIGHT = 0.60
    private const val CHOICE_VS_HABIT_WEIGHT = 0.25
    private const val SECTION_WEIGHT = 0.15

    /** Minimum combined score to count as a "strong" pattern-sibling by default. */
    const val DEFAULT_THRESHOLD = 0.55

    fun linksFor(
        marker: Marker,
        candidates: List<Marker>,
        threshold: Double = DEFAULT_THRESHOLD,
    ): List<PatternLink> =
        candidates
            .asSequence()
            .filter { it.id != marker.id }
            .map { candidate -> link(marker, candidate) }
            .filter { it.score >= threshold }
            .sortedByDescending { it.score }
            .toList()

    private fun link(a: Marker, b: Marker): PatternLink {
        val emotionSimilarity = cosineSimilarity(a.emotionSignature.asVector(), b.emotionSignature.asVector())
        val sharedChoiceVsHabit = a.choiceVsHabit == b.choiceVsHabit && a.choiceVsHabit != ChoiceVsHabit.UNCLEAR
        val sharedSection = a.sourceSection == b.sourceSection

        val score = EMOTION_WEIGHT * emotionSimilarity +
            (if (sharedChoiceVsHabit) CHOICE_VS_HABIT_WEIGHT else 0.0) +
            (if (sharedSection) SECTION_WEIGHT else 0.0)

        return PatternLink(
            markerId = a.id,
            siblingMarkerId = b.id,
            score = score,
            emotionSimilarity = emotionSimilarity,
            sharedChoiceVsHabit = sharedChoiceVsHabit,
            sharedSection = sharedSection,
        )
    }

    private fun cosineSimilarity(a: List<Double>, b: List<Double>): Double {
        require(a.size == b.size) { "Emotion vectors must be the same dimensionality" }
        var dot = 0.0
        var normA = 0.0
        var normB = 0.0
        for (i in a.indices) {
            dot += a[i] * b[i]
            normA += a[i] * a[i]
            normB += b[i] * b[i]
        }
        if (normA == 0.0 || normB == 0.0) return 0.0
        return dot / (sqrt(normA) * sqrt(normB))
    }
}
