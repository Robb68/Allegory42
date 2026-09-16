package com.realm444.android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.realm444.android.AppContainer
import com.realm444.core.model.ChoiceVsHabit
import com.realm444.core.model.Marker
import com.realm444.core.model.Section
import com.realm444.core.state.Action
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.util.UUID
import com.realm444.android.LOCAL_USER_ID

/**
 * MILESTONE 2 PLACEHOLDER RENDER — a plain list, exactly per the build
 * sequence's own wording for this step ("placeholder render (plain markers,
 * no video-matching visuals yet)"). This screen's only job is to prove data
 * flow end to end: the FAB logs a marker into the Store (instant UI update)
 * and persists it via the repository; markers loaded from SQLDelight at app
 * start already round-trip through here. The star-cluster render replaces
 * this screen's content in Milestone 3 — the Scaffold/nav shell around it
 * is what survives.
 */
@Composable
fun MarkerListScreen(container: AppContainer) {
    val state by container.store.state.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Realm 444 — markers (placeholder)") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val marker = syntheticTestMarker()
                container.store.dispatch(Action.LogMarker(marker))
                scope.launch { container.markerRepository.insert(marker) }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Log test marker")
            }
        },
    ) { padding ->
        if (state.markers.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No markers yet — tap + to log a test one.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(12.dp),
            ) {
                items(state.markers.reversed(), key = { it.id }) { marker ->
                    MarkerRow(marker)
                }
            }
        }
    }
}

@Composable
private fun MarkerRow(marker: Marker) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(marker.title, style = MaterialTheme.typography.titleMedium)
            Text(
                "${marker.sourceSection.name} · ${marker.choiceVsHabit.name} · ${marker.timestamp}",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

private fun syntheticTestMarker(): Marker = Marker(
    id = UUID.randomUUID().toString(),
    userId = LOCAL_USER_ID,
    timestamp = Clock.System.now(),
    sourceSection = Section.entries.random(),
    title = "Test marker logged from the Milestone 2 placeholder screen",
    choiceVsHabit = ChoiceVsHabit.entries.random(),
    choiceVsHabitConfidence = 0.5,
)
