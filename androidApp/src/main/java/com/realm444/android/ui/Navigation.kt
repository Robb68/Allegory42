package com.realm444.android.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.realm444.android.AppContainer

/**
 * One destination today (the Milestone 2 placeholder list). The NavHost
 * exists now, ahead of need, specifically so Milestone 3's star-cluster
 * scene and later milestones' mind-map card / patterns view / radar screens
 * are additions here, not a navigation rewrite.
 */
object Routes {
    const val MARKERS = "markers"
}

@Composable
fun Realm444NavHost(
    container: AppContainer,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = Routes.MARKERS) {
        composable(Routes.MARKERS) {
            MarkerListScreen(container)
        }
    }
}
