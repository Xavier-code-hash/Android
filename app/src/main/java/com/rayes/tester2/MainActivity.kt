package com.rayes.tester2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.rayes.tester2.navigation.AppNavHost
import com.rayes.tester2.ui.theme.AppTheme
import com.rayes.tester2.ui.theme.LocalAppTheme
import com.rayes.tester2.ui.theme.Tester2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeState = remember { mutableStateOf(AppTheme.DEFAULT) }
            
            CompositionLocalProvider(LocalAppTheme provides themeState) {
                Tester2Theme {
                    AppNavHost()
                }
            }
        }
    }
}
