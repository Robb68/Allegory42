package com.realm444.android

import android.app.Application
import com.realm444.android.persistence.DriverFactory
import com.realm444.core.persistence.MarkerRepository
import com.realm444.core.persistence.MeaningStatementRepository
import com.realm444.core.persistence.MindMapNodeRepository
import com.realm444.core.persistence.Realm444Database
import com.realm444.core.persistence.SqlDelightMarkerRepository
import com.realm444.core.persistence.SqlDelightMeaningStatementRepository
import com.realm444.core.persistence.SqlDelightMindMapNodeRepository
import com.realm444.core.state.AppState
import com.realm444.core.state.Store
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * No multi-user/social features are in scope (Section 8) — one local user
 * per install. A fixed id is enough; there is no account system to derive
 * one from.
 */
const val LOCAL_USER_ID = "local-user"

/**
 * The composition root — the one place /core's Store, repositories, and the
 * platform SqlDriver are wired together. Everything below this class reads
 * [container] rather than constructing its own dependencies.
 *
 * MILESTONE 2 NOTE: hydrating the store synchronously in [onCreate] via
 * [runBlocking] is a placeholder to prove the persistence round-trip
 * end-to-end (Build Sequence step 2) without pulling in a splash-screen /
 * loading-state pattern that's out of scope for this milestone. It should
 * be replaced with an async startup (loading state in the Compose shell,
 * or a SplashScreen) before this ships — flagging here rather than quietly
 * leaving a blocking call in a "done" build.
 */
class Realm444Application : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        val driver = DriverFactory.create(this)
        val database = Realm444Database(driver)
        val markerRepository = SqlDelightMarkerRepository(database)
        val mindMapNodeRepository = SqlDelightMindMapNodeRepository(database)
        val meaningStatementRepository = SqlDelightMeaningStatementRepository(database)

        val initialMarkers = runBlocking { markerRepository.observeAll().first() }
        val initialMeaningStatement = runBlocking { meaningStatementRepository.get(LOCAL_USER_ID) }

        val store = Store(
            AppState(
                markers = initialMarkers,
                meaningStatement = initialMeaningStatement,
            )
        )

        container = AppContainer(
            store = store,
            markerRepository = markerRepository,
            mindMapNodeRepository = mindMapNodeRepository,
            meaningStatementRepository = meaningStatementRepository,
        )
    }
}

data class AppContainer(
    val store: Store,
    val markerRepository: MarkerRepository,
    val mindMapNodeRepository: MindMapNodeRepository,
    val meaningStatementRepository: MeaningStatementRepository,
)
