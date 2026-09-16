package com.realm444.core.model

/**
 * The twelve capture sections. All feed the same marker stream — no tab bar,
 * no twelve navigation destinations (Manus Build Prompt Section 5).
 */
enum class Section {
    HONESTY,
    HOPE,
    FAITH,
    COURAGE,
    INTEGRITY,
    OPEN_MINDEDNESS,
    HUMILITY,
    LOVE,
    JUSTICE,
    PERSEVERANCE,
    SPIRITUAL_AWARENESS,
    SERVICE;

    /** What each section captures, verbatim from the Manus Build Prompt Section 12 table. */
    val description: String
        get() = when (this) {
            HONESTY -> "The plain, truthful account of what happened — no spin"
            HOPE -> "What you're moving toward"
            FAITH -> "What you're willing to trust before it's proven"
            COURAGE -> "Action taken through fear, not the absence of it"
            INTEGRITY -> "Whether behavior matched stated values"
            OPEN_MINDEDNESS -> "Willingness to consider what you'd normally dismiss"
            HUMILITY -> "Accurate self-sizing — neither inflated nor diminished"
            LOVE -> "The state of actual relationships, not their performance"
            JUSTICE -> "Where correction or amends are due"
            PERSEVERANCE -> "Showing up again after not wanting to"
            SPIRITUAL_AWARENESS -> "Presence and stillness — prayer, meditation, contemplation"
            SERVICE -> "What was given today that wasn't asked of you"
        }
}
