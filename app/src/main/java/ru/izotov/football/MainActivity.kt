package ru.izotov.football

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.izotov.football.ui.theme.FootballTheme
import ru.izotov.football.views.GameView
import ru.izotov.football.views.Screen1
import ru.izotov.football.views.Screen2
import ru.izotov.football.views.Screen3


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
    
            val navController = rememberNavController()
    
            FootballTheme {
                // A surface container using the 'background' color from the theme
                NavHost(
                    navController = navController,
                    startDestination = "screen_1"
                ) {
                    composable("screen_1") {
                        Screen1 {
                            navController.navigate("screen_2")
                        }
                    }
                    composable("screen_2") {
                        Screen2(this@MainActivity)
                    }
                    composable("screen_3") {
                        Screen3 {
                            navController.navigate("screen_1") {
                                popUpTo("screen_1")
                            }
                    
                        }
                    }
                }
            }
        }
    }
}

