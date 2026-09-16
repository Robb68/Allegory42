package com.realm444.core.content

import com.realm444.core.model.Section

/**
 * One of the 51 locked reflection prompts. Text is verbatim from
 * "Realm 444 — Section 9 Reflection Prompts (Final 51)" — do not regenerate
 * or rephrase (v5 Section 9).
 */
data class ReflectionPrompt(
    /** 1-based position in the locked, authored set of 51 — stable, never renumbered. */
    val number: Int,
    val section: Section,
    val text: String,
)

/**
 * The 51 locked reflection prompts, verbatim, grouped by section.
 *
 * NOTE — locked-content gap: the source doc titles itself "Final 51" and the
 * count here is exactly 51, but SERVICE has zero prompts in the source
 * material (only its Section 12 description exists) and SPIRITUAL_AWARENESS
 * has exactly one. This is reproduced as-authored, not filled in — flagging
 * per the build prompt's instruction to report load-bearing gaps rather than
 * silently inventing content for a locked set.
 */
object ReflectionPrompts {

    val all: List<ReflectionPrompt> = listOf(
        // HONESTY — the plain, truthful account of what happened, no spin
        ReflectionPrompt(1, Section.HONESTY, "What did you do today that you'd describe differently to a friend than to yourself?"),
        ReflectionPrompt(2, Section.HONESTY, "What's the version of today you're hoping nobody asks about?"),
        ReflectionPrompt(3, Section.HONESTY, "What did you leave out when you told someone how you were doing?"),
        ReflectionPrompt(4, Section.HONESTY, "What did you embellish today, and what did you downplay?"),
        ReflectionPrompt(5, Section.HONESTY, "What did you tell yourself was fine that isn't?"),

        // HOPE — what you're moving toward
        ReflectionPrompt(6, Section.HOPE, "What did you do today because you believe tomorrow's worth it?"),
        ReflectionPrompt(7, Section.HOPE, "What are you working toward that you haven't said out loud yet?"),
        ReflectionPrompt(8, Section.HOPE, "What kept you going today when it would have been easier to quit?"),
        ReflectionPrompt(9, Section.HOPE, "What would you start now if you knew it wouldn't be wasted?"),
        ReflectionPrompt(10, Section.HOPE, "What small thing today pointed in the direction you actually want to go?"),

        // FAITH — what you're willing to trust before it's proven
        ReflectionPrompt(11, Section.FAITH, "What did you act on today without proof it would work?"),
        ReflectionPrompt(12, Section.FAITH, "Where did you trust someone who couldn't guarantee the outcome?"),
        ReflectionPrompt(13, Section.FAITH, "What are you still holding onto that you can't yet see the reason for?"),
        ReflectionPrompt(14, Section.FAITH, "What did you let go of today, trusting you didn't need to grip it so hard?"),
        ReflectionPrompt(15, Section.FAITH, "What would you do differently today if you trusted things would work out?"),

        // COURAGE — action taken through fear, not the absence of it
        ReflectionPrompt(16, Section.COURAGE, "What did you do today that scared you, and you did it anyway?"),
        ReflectionPrompt(17, Section.COURAGE, "What conversation did you stop avoiding today?"),
        ReflectionPrompt(18, Section.COURAGE, "Where did you speak up when staying quiet would have been easier?"),
        ReflectionPrompt(19, Section.COURAGE, "What fear did you brace for today that never came?"),
        ReflectionPrompt(20, Section.COURAGE, "What did you risk today that could have changed your future?"),

        // INTEGRITY — whether behavior matched stated values
        ReflectionPrompt(21, Section.INTEGRITY, "Did you act like the person you want to be today?"),
        ReflectionPrompt(22, Section.INTEGRITY, "Where did your actions not match what you believe today?"),
        ReflectionPrompt(23, Section.INTEGRITY, "What did you do today that you'd be uncomfortable explaining?"),
        ReflectionPrompt(24, Section.INTEGRITY, "Where did you cut a corner today when nobody was watching?"),
        ReflectionPrompt(25, Section.INTEGRITY, "Did your private self and your public self match today?"),

        // OPEN-MINDEDNESS — willingness to consider what you'd normally dismiss
        ReflectionPrompt(26, Section.OPEN_MINDEDNESS, "What did someone say today that you dismissed?"),
        ReflectionPrompt(27, Section.OPEN_MINDEDNESS, "What did you assume today that you didn't actually check?"),
        ReflectionPrompt(28, Section.OPEN_MINDEDNESS, "Whose perspective did you disregard today without really hearing it?"),
        ReflectionPrompt(29, Section.OPEN_MINDEDNESS, "What might you be wrong about that you've never questioned?"),
        ReflectionPrompt(30, Section.OPEN_MINDEDNESS, "What changed your mind today, even a little?"),

        // HUMILITY — accurate self-sizing, neither inflated nor diminished
        ReflectionPrompt(31, Section.HUMILITY, "What did you take credit for today that wasn't all you?"),
        ReflectionPrompt(32, Section.HUMILITY, "Where did you need help today and actually ask for it?"),
        ReflectionPrompt(33, Section.HUMILITY, "What did you get wrong today that you'd rather not admit?"),
        ReflectionPrompt(34, Section.HUMILITY, "Where did you overestimate or underestimate yourself today?"),
        ReflectionPrompt(35, Section.HUMILITY, "What did you learn today from someone you didn't expect to?"),

        // LOVE — the state of actual relationships, not their performance
        ReflectionPrompt(36, Section.LOVE, "Who did you actually show up for today, not just say you care about?"),
        ReflectionPrompt(37, Section.LOVE, "Where did you act on kindness today instead of just feeling it?"),
        ReflectionPrompt(38, Section.LOVE, "Who needed something from you today that you didn't notice?"),
        ReflectionPrompt(39, Section.LOVE, "Where did you keep score today instead of just giving?"),
        ReflectionPrompt(40, Section.LOVE, "Who did you take for granted today?"),

        // JUSTICE — where correction or amends are due
        ReflectionPrompt(41, Section.JUSTICE, "Who do you owe an apology to that you haven't given yet?"),
        ReflectionPrompt(42, Section.JUSTICE, "Where were you unfair today and knew it?"),
        ReflectionPrompt(43, Section.JUSTICE, "What wrong did you see today and say nothing about?"),
        ReflectionPrompt(44, Section.JUSTICE, "What do you need to make right that time won't fix on its own?"),
        ReflectionPrompt(45, Section.JUSTICE, "Where did you blame someone else for something that was yours?"),

        // PERSEVERANCE — showing up again after not wanting to
        ReflectionPrompt(46, Section.PERSEVERANCE, "What did you show up for today that you really didn't want to?"),
        ReflectionPrompt(47, Section.PERSEVERANCE, "What did you try again today after it didn't work before?"),
        ReflectionPrompt(48, Section.PERSEVERANCE, "What made you want to quit today, and what kept you going?"),
        ReflectionPrompt(49, Section.PERSEVERANCE, "What are you still working on that most people would have dropped by now?"),
        ReflectionPrompt(50, Section.PERSEVERANCE, "What got harder today but you didn't stop?"),

        // SPIRITUAL AWARENESS — presence and stillness (prayer, meditation, contemplation)
        ReflectionPrompt(51, Section.SPIRITUAL_AWARENESS, "What unnecessary burdens are you carrying around now that you haven't put down?"),
    )

    private val bySection: Map<Section, List<ReflectionPrompt>> = all.groupBy { it.section }

    fun forSection(section: Section): List<ReflectionPrompt> = bySection[section].orEmpty()
}
