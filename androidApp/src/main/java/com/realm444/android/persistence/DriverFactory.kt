package com.realm444.android.persistence

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.realm444.core.persistence.Realm444Database

/**
 * The one platform-specific piece of persistence /androidApp owns (Section
 * 3): the [SqlDriver] implementation. Everything downstream — schema,
 * queries, repositories — is commonMain and already lives in /core.
 */
object DriverFactory {
    fun create(context: Context): SqlDriver =
        AndroidSqliteDriver(Realm444Database.Schema, context.applicationContext, "realm444.db")
}
