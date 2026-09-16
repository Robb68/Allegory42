package com.realm444.core.engine

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

/**
 * A marker's position along the spiral: derived, never hand-stored, always a
 * deterministic function of chronological order (Section 4).
 *
 * [angle] and [radius] are the 2D baseline owned by /core. Everything about
 * how this is presented in 3D space — z-depth, camera, star-cluster jitter —
 * belongs to /androidApp/render, never here.
 */
data class SpiralPosition(
    val turnIndex: Int,
    val angle: Double,
    val radius: Double,
    val x: Double,
    val y: Double,
)

/**
 * Pure spiral position math, preserved as the deterministic baseline from the
 * spiral.html prototype (Manus Build Prompt Section 4):
 *
 * ```
 * th = 0.6 + i * STEP      // i = chronological index, STEP = 0.82 rad/marker
 * r  = R0 + K * th          // R0 = 34, K = 10.5
 * x, y = cos(th) * r, sin(th) * r
 * ```
 *
 * A pure function of marker order — no I/O, no platform imports. This is the
 * classpath correctness test: this file must compile with zero Android SDK
 * on the classpath.
 */
object SpiralEngine {
    private const val THETA_OFFSET = 0.6
    private const val STEP = 0.82
    private const val R0 = 34.0
    private const val K = 10.5

    /**
     * @param chronologicalIndex 0-based position of the marker within the
     *   full ordered marker list (oldest first).
     */
    fun positionFor(chronologicalIndex: Int): SpiralPosition {
        require(chronologicalIndex >= 0) { "chronologicalIndex must be >= 0, was $chronologicalIndex" }

        val theta = THETA_OFFSET + chronologicalIndex * STEP
        val radius = R0 + K * theta
        val x = cos(theta) * radius
        val y = sin(theta) * radius
        val turnIndex = floor(theta / (2 * PI)).toInt()

        return SpiralPosition(
            turnIndex = turnIndex,
            angle = theta,
            radius = radius,
            x = x,
            y = y,
        )
    }

    /**
     * Positions for every marker in [chronologicallyOrderedMarkerIds], oldest
     * first, keyed by marker id. Convenience for callers that already hold
     * the full ordered id list (e.g. the state store).
     */
    fun positionsFor(chronologicallyOrderedMarkerIds: List<String>): Map<String, SpiralPosition> =
        chronologicallyOrderedMarkerIds
            .withIndex()
            .associate { (index, id) -> id to positionFor(index) }
}
