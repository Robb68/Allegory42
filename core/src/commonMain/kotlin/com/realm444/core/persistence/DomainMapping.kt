package com.realm444.core.persistence

import com.realm444.core.model.AnchorStatus
import com.realm444.core.model.ChoiceVsHabit
import com.realm444.core.model.Emotion
import com.realm444.core.model.EmotionSignature
import com.realm444.core.model.NodeType
import com.realm444.core.model.Section
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import com.realm444.core.model.Marker as DomainMarker
import com.realm444.core.model.MeaningStatement as DomainMeaningStatement
import com.realm444.core.model.MindMapNode as DomainMindMapNode

/** Row <-> domain-model conversions. The only place either generated SQLDelight types or storage encodings (epoch millis, comma-joined tags) are allowed to leak out of. */

private fun List<String>.encodeTags(): String = joinToString(",")
private fun String.decodeTags(): List<String> = if (isEmpty()) emptyList() else split(",")

internal fun Marker.toDomain(): DomainMarker = DomainMarker(
    id = id,
    userId = user_id,
    timestamp = Instant.fromEpochMilliseconds(timestamp_epoch_millis),
    sourceSection = Section.valueOf(source_section),
    title = title,
    primaryFeelingTags = primary_feeling_tags.decodeTags(),
    choiceVsHabit = ChoiceVsHabit.valueOf(choice_vs_habit),
    choiceVsHabitConfidence = choice_vs_habit_confidence,
    isAnchor = is_anchor,
    targetDate = target_date?.let(LocalDate::parse),
    anchorStatus = anchor_status?.let(AnchorStatus::valueOf),
    emotionSignature = EmotionSignature.of(
        Emotion.HAPPINESS to emotion_happiness,
        Emotion.SADNESS to emotion_sadness,
        Emotion.ANGER to emotion_anger,
        Emotion.JOY to emotion_joy,
        Emotion.EXCITEMENT to emotion_excitement,
        Emotion.ANTICIPATION to emotion_anticipation,
        Emotion.ANXIETY to emotion_anxiety,
    ),
)

internal fun Mind_map_node.toDomain(): DomainMindMapNode = DomainMindMapNode(
    id = id,
    markerId = marker_id,
    parentNodeId = parent_node_id,
    nodeType = NodeType.valueOf(node_type),
    content = content,
    positionHint = position_hint,
    createdAt = Instant.fromEpochMilliseconds(created_at_epoch_millis),
    editedAt = Instant.fromEpochMilliseconds(edited_at_epoch_millis),
)

internal fun Meaning_statement.toDomain(): DomainMeaningStatement = DomainMeaningStatement(
    id = id,
    userId = user_id,
    text = text,
    createdAt = Instant.fromEpochMilliseconds(created_at_epoch_millis),
    editedAt = Instant.fromEpochMilliseconds(edited_at_epoch_millis),
)

internal fun DomainMarker.tagsEncoded(): String = primaryFeelingTags.encodeTags()
