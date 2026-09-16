package com.realm444.core.persistence

import com.realm444.core.model.Marker
import com.realm444.core.model.MeaningStatement
import com.realm444.core.model.MindMapNode
import kotlinx.coroutines.flow.Flow

/**
 * Domain-facing persistence contracts. Implementations live in commonMain
 * too (the generated SQLDelight database is platform-agnostic) — only the
 * `SqlDriver` itself is platform-specific, and that's provided by
 * /androidApp/persistence, never referenced here.
 */
interface MarkerRepository {
    /** Chronological, oldest first — the order SpiralEngine derives position from. */
    fun observeAll(): Flow<List<Marker>>
    suspend fun getById(id: String): Marker?
    suspend fun insert(marker: Marker)
    suspend fun update(marker: Marker)
    suspend fun delete(markerId: String)
}

interface MindMapNodeRepository {
    suspend fun getForMarker(markerId: String): List<MindMapNode>
    suspend fun insert(node: MindMapNode)
    suspend fun update(node: MindMapNode)
    suspend fun delete(nodeId: String)
}

interface MeaningStatementRepository {
    suspend fun get(userId: String): MeaningStatement?
    suspend fun upsert(statement: MeaningStatement)
}
