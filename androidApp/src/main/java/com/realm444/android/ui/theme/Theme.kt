package com.realm444.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * MILESTONE 2 PLACEHOLDER — deliberately Material3's stock color scheme, not
 * a guess at the locked burgundy/maroon/gold-on-near-black palette (v4
 * Section 5.2 / v5 Section 5.2). That palette's exact hex/rgba values are a
 * gap in the source material handed off so far; the visual pass is Milestone
 * 3's job, not this one's.
 */
@Composable
fun Realm444Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    MaterialTheme(colorScheme = colorScheme, content = content)
}
