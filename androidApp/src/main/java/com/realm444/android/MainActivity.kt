package com.realm444.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.realm444.android.ui.Realm444NavHost
import com.realm444.android.ui.theme.Realm444Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = (application as Realm444Application).container

        setContent {
            Realm444Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Realm444NavHost(container)
                }
            }
        }
    }
}
