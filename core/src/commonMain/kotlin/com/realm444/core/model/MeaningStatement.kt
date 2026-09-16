package com.realm444.core.model

import kotlinx.datetime.Instant

/**
 * The "why are we here" statement — captured once in the user's own words,
 * pinned to the spiral/cluster center, editable, and resurfaced periodically
 * against real logging behavior. Never prewritten copy.
 */
data class MeaningStatement(
    val id: String,
    val userId: String,
    val text: String,
    val createdAt: Instant,
    val editedAt: Instant,
)
