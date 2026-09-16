package com.realm444.core.model

/**
 * The seven tracked emotions (v5 Section 4 / 7.1). Fixed set — the pattern
 * engine depends on every marker's signature spanning the same dimensions.
 */
enum class Emotion {
    HAPPINESS,
    SADNESS,
    ANGER,
    JOY,
    EXCITEMENT,
    ANTICIPATION,
    ANXIETY
}

/**
 * A map of the seven tracked emotions to intensity 0.0–1.0, captured from the
 * user's spoken input at capture time and editable after the fact like any
 * other field (v5 Section 4). Always dense — every [Emotion] has an entry,
 * defaulting to 0.0 when not expressed.
 */
data class EmotionSignature(private val intensities: Map<Emotion, Double>) {

    init {
        for (emotion in Emotion.entries) {
            val value = intensities[emotion]
            require(value != null) { "EmotionSignature is missing an entry for $emotion" }
            require(value in 0.0..1.0) { "Intensity for $emotion must be in 0.0..1.0, was $value" }
        }
    }

    operator fun get(emotion: Emotion): Double = intensities.getValue(emotion)

    /** The emotion(s) with the highest intensity. Empty signatures (all-zero) return all seven. */
    fun dominant(): Set<Emotion> {
        val max = intensities.values.max()
        return intensities.filterValues { it == max }.keys
    }

    fun asVector(): List<Double> = Emotion.entries.map { intensities.getValue(it) }

    companion object {
        val EMPTY = EmotionSignature(Emotion.entries.associateWith { 0.0 })

        fun of(vararg pairs: Pair<Emotion, Double>): EmotionSignature {
            val base = Emotion.entries.associateWith { 0.0 }
            return EmotionSignature(base + pairs)
        }
    }
}
