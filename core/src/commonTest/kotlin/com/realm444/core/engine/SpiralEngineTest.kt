package com.realm444.core.engine

import kotlin.math.PI
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SpiralEngineTest {

    private fun assertClose(expected: Double, actual: Double, message: String, epsilon: Double = 1e-9) {
        assertTrue(abs(expected - actual) < epsilon, "$message: expected $expected, was $actual")
    }

    @Test
    fun firstMarkerMatchesLockedFormula() {
        // th = 0.6 + 0 * 0.82 = 0.6 ; r = 34 + 10.5 * 0.6 = 40.3
        val position = SpiralEngine.positionFor(0)
        assertClose(0.6, position.angle, "angle")
        assertClose(40.3, position.radius, "radius")
        assertClose(kotlin.math.cos(0.6) * 40.3, position.x, "x")
        assertClose(kotlin.math.sin(0.6) * 40.3, position.y, "y")
    }

    @Test
    fun tenthMarkerMatchesLockedFormula() {
        // i = 9 : th = 0.6 + 9 * 0.82 = 7.98 ; r = 34 + 10.5 * 7.98 = 117.79
        val position = SpiralEngine.positionFor(9)
        assertClose(7.98, position.angle, "angle")
        assertClose(117.79, position.radius, "radius")
    }

    @Test
    fun radiusGrowsMonotonicallyWithIndex() {
        val radii = (0 until 50).map { SpiralEngine.positionFor(it).radius }
        for (i in 1 until radii.size) {
            assertTrue(radii[i] > radii[i - 1], "radius must strictly increase with marker order")
        }
    }

    @Test
    fun turnIndexAdvancesEveryFullRevolution() {
        // theta crosses 2*PI (~6.283) between index 6 (th=5.52) and index 7 (th=6.34)
        assertEquals(0, SpiralEngine.positionFor(6).turnIndex)
        assertEquals(1, SpiralEngine.positionFor(7).turnIndex)
        assertTrue(SpiralEngine.positionFor(7).angle > 2 * PI)
    }

    @Test
    fun rejectsNegativeIndex() {
        assertFailsWith<IllegalArgumentException> { SpiralEngine.positionFor(-1) }
    }

    @Test
    fun positionsForIsDeterministicAndOrderPreserving() {
        val ids = listOf("a", "b", "c")
        val positions = SpiralEngine.positionsFor(ids)
        assertEquals(SpiralEngine.positionFor(0), positions.getValue("a"))
        assertEquals(SpiralEngine.positionFor(1), positions.getValue("b"))
        assertEquals(SpiralEngine.positionFor(2), positions.getValue("c"))
    }
}
