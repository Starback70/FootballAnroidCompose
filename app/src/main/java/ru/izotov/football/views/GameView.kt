@file:Suppress("UNUSED_EXPRESSION")

package ru.izotov.football.views

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import ru.izotov.football.R
import ru.izotov.football.controller.FIELD_WIDTH
import ru.izotov.football.controller.Logic
import ru.izotov.football.controller.ballCoordinate
import ru.izotov.football.controller.currentPlayer
import ru.izotov.football.controller.currentPlayerName
import ru.izotov.football.controller.lineList
import ru.izotov.football.controller.params
import ru.izotov.football.controller.player1
import ru.izotov.football.controller.player2
import ru.izotov.football.controller.playerScope1
import ru.izotov.football.controller.playerScope2
import ru.izotov.football.controller.pointList
import ru.izotov.football.models.Direction
import ru.izotov.football.models.Direction.DOWN
import ru.izotov.football.models.Direction.LEFT
import ru.izotov.football.models.Direction.RIGHT
import ru.izotov.football.models.Direction.UP
import ru.izotov.football.ui.theme.Black
import ru.izotov.football.ui.theme.Gray200
import ru.izotov.football.ui.theme.Green


class GameView(context: Context) {
    private val logic = Logic(context)
    private val buttonSize = 50.dp
    private val buttonBorder = 3.dp
    
    @Composable
    fun GameScreen() {/* --------------------------- field ------------------------------ */
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow),
            
            ) {
            /* ------------------------------- Info ------------------------------ */
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.05f)
                    .padding(5.dp),
            ) {
                Text(text = "$playerScope1", color = player1.color)
                Text(text = "Ход: $currentPlayerName", color = currentPlayer.color)
                Text(text = "$playerScope2", color = player2.color)
            }
            /* ------------------------------- Field --------------------------------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .fillMaxHeight(0.75f)
                    .background(Green)
                    .padding(10.dp),
            ) {
                DrawField()
                DrawBallAndLine()
            }
            /* ------------------------------- Keyboard ------------------------------ */
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                ) {
                    /* --------------------------- UP -------------------------------- */
                    ButtonMove((UP), R.drawable.ic_arrow_up)
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Green)
                ) {
                    /* --------------------------- LEFT ------------------------------ */
                    ButtonMove((LEFT), R.drawable.ic_arrow_left)
                    /* --------------------------- SHOT ------------------------------ */
                    ButtonShot(R.drawable.ic_shot)
                    /* --------------------------- RIGHT ------------------------------ */
                    ButtonMove((RIGHT), R.drawable.ic_arrow_right)
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Magenta)
                ) {
                    /* --------------------------- DOWN ------------------------------ */
                    ButtonMove((DOWN), R.drawable.ic_arrow_down)
                }
            }
        }
    }
    
    @Composable
    fun ButtonMove(direction: Direction, iconId: Int) {
        IconButton(
            onClick = { logic.ballMove(direction) },
            modifier = Modifier
                .size(buttonSize, buttonSize)
                .padding(5.dp)
                .border(BorderStroke(buttonBorder, Black))
                .background(Gray200),
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
    
    @Composable
    fun ButtonShot(iconId: Int) {
        IconButton(
            onClick = { logic.shot() },
            modifier = Modifier
                .size(buttonSize, buttonSize)
                .padding(5.dp)
                .border(BorderStroke(buttonBorder, Black))
                .background(Gray200),
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
    
    /* -------------------------------------- drawing Ball and Line----------------------------------------- */
    @Composable
    fun DrawBallAndLine() {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cell = size.width / 8
            val lineWidth = cell / 10
            /* --------------------------- drawing Lines ------------------------------ */
            lineList.forEach {
                drawLine(
                    start = Offset(it.start.x * cell, it.start.y * cell),
                    end = Offset(it.end.x * cell, it.end.y * cell),
                    color = it.color,
                    strokeWidth = lineWidth
                )
            }/* --------------------------- drawing Ball ------------------------------ */
            drawCircle(
                color = Color.Yellow,
                center = Offset(
                    pointList.last().x * cell, pointList.last().y * cell
                ),
                radius = cell / 5,
            )
//            drawImage(
//                image = ImageBitmap.imageResource(id = R.drawable.ball),
//                topLeft = Offset(pointList.last().x * cell, pointList.last().y * cell),
//                /*@FloatRange(from = 0.0, to = 1.0)*/
//                alpha = 1.0f,
//                style = Fill,
//            )
            /* --------------------------- drawing Player ---------------------------- */
            drawCircle(
                color = currentPlayer.color,
                center = Offset(ballCoordinate.x * cell, ballCoordinate.y * cell),
                radius = cell / 4,
                alpha = 0.75f
            )
        }
    }
    
    /* -------------------------------------- drawing Field----------------------------------------- */
    @Composable
    fun DrawField() {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Green)
        ) {
            val cell = size.width / 8
            val lineWidth = cell / 15
            params.borderLines.forEach {
                drawLine(
                    color = it.color,
                    strokeWidth = lineWidth,
                    start = Offset(it.start.x * cell, it.start.y * cell),
                    end = Offset(it.end.x * cell, it.end.y * cell)
                )
            }
        }
    }
    
}