package com.realm444.core.model

/**
 * Choice vs. habit is first-class data — it drives both the visual encoding
 * on the spiral/cluster and the patterns view. Not a tag buried in text.
 */
enum class ChoiceVsHabit {
    CHOICE,
    HABIT,
    UNCLEAR
}

/** Anchor markers persist and get revisited over time (any section, user-settable). */
enum class AnchorStatus {
    ACTIVE,
    ACHIEVED,
    ABANDONED
}
