package ru.izotov.football

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            val player1 = remember {
                Player("Красный", Color.Red, 0, Point(4, 12))
            }
            val player2 = remember {
                Player("Красный", Color.Red, 0, Point(4, 0))
            }
            val ballCoordinate = remember {
                Offset(0f, 0f)
            }
            Draw()
        }
    }
}

