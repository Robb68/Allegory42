package com.realm444.core.persistence

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.realm444.core.model.Emotion
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.realm444.core.model.Marker as DomainMarker
import com.realm444.core.model.MeaningStatement as DomainMeaningStatement
import com.realm444.core.model.MindMapNode as DomainMindMapNode

/**
 * SQLDelight-backed implementations. These live in commonMain, not
 * /androidApp — the generated [Realm444Database] is platform-agnostic; only
 * the [app.cash.sqldelight.db.SqlDriver] passed in at construction is
 * platform-specific (AndroidSqliteDriver on Android), and that's supplied by
 * the caller in /androidApp/persistence.
 */
class SqlDelightMarkerRepository(
    private val database: Realm444Database,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : MarkerRepository {

    private val queries get() = database.realm444Queries

    override fun observeAll(): Flow<List<DomainMarker>> =
        queries.selectAllMarkers().asFlow().mapToList(ioDispatcher).map { rows -> rows.map { it.toDomain() } }

    override suspend fun getById(id: String): DomainMarker? = withContext(ioDispatcher) {
        queries.selectMarkerById(id).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun insert(marker: DomainMarker) = withContext(ioDispatcher) {
        queries.insertMarker(
            id = marker.id,
            user_id = marker.userId,
            timestamp_epoch_millis = marker.timestamp.toEpochMilliseconds(),
            source_section = marker.sourceSection.name,
            title = marker.title,
            primary_feeling_tags = marker.tagsEncoded(),
            choice_vs_habit = marker.choiceVsHabit.name,
            choice_vs_habit_confidence = marker.choiceVsHabitConfidence,
            is_anchor = marker.isAnchor,
            target_date = marker.targetDate?.toString(),
            anchor_status = marker.anchorStatus?.name,
            emotion_happiness = marker.emotionSignature[Emotion.HAPPINESS],
            emotion_sadness = marker.emotionSignature[Emotion.SADNESS],
            emotion_anger = marker.emotionSignature[Emotion.ANGER],
            emotion_joy = marker.emotionSignature[Emotion.JOY],
            emotion_excitement = marker.emotionSignature[Emotion.EXCITEMENT],
            emotion_anticipation = marker.emotionSignature[Emotion.ANTICIPATION],
            emotion_anxiety = marker.emotionSignature[Emotion.ANXIETY],
        )
    }

    override suspend fun update(marker: DomainMarker) = withContext(ioDispatcher) {
        queries.updateMarker(
            title = marker.title,
            primary_feeling_tags = marker.tagsEncoded(),
            choice_vs_habit = marker.choiceVsHabit.name,
            choice_vs_habit_confidence = marker.choiceVsHabitConfidence,
            is_anchor = marker.isAnchor,
            target_date = marker.targetDate?.toString(),
            anchor_status = marker.anchorStatus?.name,
            emotion_happiness = marker.emotionSignature[Emotion.HAPPINESS],
            emotion_sadness = marker.emotionSignature[Emotion.SADNESS],
            emotion_anger = marker.emotionSignature[Emotion.ANGER],
            emotion_joy = marker.emotionSignature[Emotion.JOY],
            emotion_excitement = marker.emotionSignature[Emotion.EXCITEMENT],
            emotion_anticipation = marker.emotionSignature[Emotion.ANTICIPATION],
            emotion_anxiety = marker.emotionSignature[Emotion.ANXIETY],
            id = marker.id,
        )
    }

    override suspend fun delete(markerId: String) = withContext(ioDispatcher) {
        queries.deleteMarker(markerId)
    }
}

class SqlDelightMindMapNodeRepository(
    private val database: Realm444Database,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : MindMapNodeRepository {

    private val queries get() = database.realm444Queries

    override suspend fun getForMarker(markerId: String): List<DomainMindMapNode> = withContext(ioDispatcher) {
        queries.selectNodesForMarker(markerId).executeAsList().map { it.toDomain() }
    }

    override suspend fun insert(node: DomainMindMapNode) = withContext(ioDispatcher) {
        queries.insertMindMapNode(
            id = node.id,
            marker_id = node.markerId,
            parent_node_id = node.parentNodeId,
            node_type = node.nodeType.name,
            content = node.content,
            position_hint = node.positionHint,
            created_at_epoch_millis = node.createdAt.toEpochMilliseconds(),
            edited_at_epoch_millis = node.editedAt.toEpochMilliseconds(),
        )
    }

    override suspend fun update(node: DomainMindMapNode) = withContext(ioDispatcher) {
        queries.updateMindMapNode(
            content = node.content,
            position_hint = node.positionHint,
            edited_at_epoch_millis = node.editedAt.toEpochMilliseconds(),
            id = node.id,
        )
    }

    override suspend fun delete(nodeId: String) = withContext(ioDispatcher) {
        queries.deleteMindMapNode(nodeId)
    }
}

class SqlDelightMeaningStatementRepository(
    private val database: Realm444Database,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : MeaningStatementRepository {

    private val queries get() = database.realm444Queries

    override suspend fun get(userId: String): DomainMeaningStatement? = withContext(ioDispatcher) {
        queries.selectMeaningStatement(userId).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun upsert(statement: DomainMeaningStatement) = withContext(ioDispatcher) {
        queries.upsertMeaningStatement(
            id = statement.id,
            user_id = statement.userId,
            text = statement.text,
            created_at_epoch_millis = statement.createdAt.toEpochMilliseconds(),
            edited_at_epoch_millis = statement.editedAt.toEpochMilliseconds(),
        )
    }
}
