package ru.izotov.football.views

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

var isNewGame = true
@Composable
fun Screen1(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Футбол",
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            onClick()
        }) {
            Text(text = "Новая игра")
            isNewGame = true
        }
        Button(onClick = {
            onClick()
        }) {
            Text(text = "Продолжить")
            isNewGame = false
        }
    }
}

@Composable
fun Screen2(context: Context) {
    GameView(context).GameScreen()
}

@Composable
fun Screen3(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Screen 3",
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            onClick()
        }) {
            Text(text = "Screen 1")
        }
    }
}