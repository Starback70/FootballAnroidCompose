package ru.izotov.football

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import ru.izotov.football.Direction.DOWN
import ru.izotov.football.Direction.LEFT
import ru.izotov.football.Direction.RIGHT
import ru.izotov.football.Direction.UP
import ru.izotov.football.ui.theme.Green
import kotlin.math.abs

/*------------------------------- Variables ---------------------------------------*/
val params = Parameters()
var lineList = mutableListOf<Line>()
var pointList = mutableListOf(Point(params.centerOfField.x, params.centerOfField.y))
val ballCoordinate = Point(params.centerOfField.x, params.centerOfField.y)
val player1 = Player("Красный", Color.Red, 0, params.gateOfPlayer1)
val player2 = Player("Синий", Color.Blue, 0, params.gateOfPlayer2)
var currentPlayer = player1
var currentPlayerName by mutableStateOf(currentPlayer.name)
var playerScope1 by mutableStateOf(player1.scope)
var playerScope2 by mutableStateOf(player2.scope)

/*------------------------------- GameLogic ---------------------------------------*/
fun shot() {
    val lastCoordinate = pointList.last()
    val x1 = lastCoordinate.x
    val y1 = lastCoordinate.y
    val x2 = ballCoordinate.x
    val y2 = ballCoordinate.y
    val line = Line(Point(x1, y1), Point(x2, y2), color = currentPlayer.color)
    if (isShotPermitted(lastCoordinate, line)) {
        log("Shot Permitted")
        lineList.add(line)
        checkPlayerChange()
        pointList.add(Point(x2, y2))
    }
    checkGoal()
    log("Params().fieldPoints: " + params.fieldPoints)
    log("lineList.size: " + lineList.size)
    log("lineList: " + lineList)
}

fun checkGoal() {
    if (ballCoordinate == player1.gate) {
        newRound()
        ++player2.scope
        log("Гол в ворота " + player1.name + " счёт: ${player1.scope} - ${player2.scope} ")
    } else if (ballCoordinate == player2.gate) {
        newRound()
        ++player1.scope
        log("Гол в ворота " + player2.name + " счёт: ${player1.scope} - ${player2.scope} ")
    }
    playerScope1 = player1.scope
    playerScope2 = player2.scope
}

fun newRound() {
    lineList.clear()
    pointList.clear()
    pointList.add(Point(params.centerOfField.x, params.centerOfField.y))
    ballCoordinate.x = params.centerOfField.x
    ballCoordinate.y = params.centerOfField.y
}


fun isShotPermitted(lastCoordinate: Point, line: Line): Boolean {
    val x = abs(lastCoordinate.x - ballCoordinate.x) == 1
    val y = abs(lastCoordinate.y - ballCoordinate.y) == 1
    val res = lineListNotContainsSameLine(lineList, line)
    return (x || y) && (res)
}

fun checkPlayerChange() {
    val currentBallPosition = Point(ballCoordinate.x, ballCoordinate.y)
    if (!pointList.contains(currentBallPosition) && !params.borderPoints.contains(currentBallPosition)) {
        changePlayer()
    }
}

fun changePlayer() {
    currentPlayer = if (currentPlayer === player1) {
        player2
    } else {
        player1
    }
    currentPlayerName = currentPlayer.name
}

fun lineListNotContainsSameLine(list: List<Line>, line: Line): Boolean {
    list.forEach {
        if (isSameLine(it, line)) {
            return false
        }
    }
    params.borderLines.forEach {
        if (isSameLine(it, line)) {
            return false
        }
    }
    return true
}

fun isSameLine(l1: Line, l2: Line): Boolean {
    return (l1.start.x == l2.start.x && l1.start.y == l2.start.y && l1.end.x == l2.end.x && l1.end.y == l2.end.y)
            || (l1.start.x == l2.end.x && l1.start.y == l2.end.y && l1.end.x == l2.start.x && l1.end.y == l2.start.y)
}

fun isPermittedPoint(point: Point): Boolean {
    return params.fieldPoints.contains(point)
}

fun ballMove(direction: Direction) {
    val newBallCoordinate = Point(ballCoordinate.x, ballCoordinate.y)
    when (direction) {
        UP -> {
            newBallCoordinate.y = ballCoordinate.y - 1
        }
        
        DOWN -> {
            newBallCoordinate.y = ballCoordinate.y + 1
        }
        
        LEFT -> {
            newBallCoordinate.x = ballCoordinate.x - 1
        }
        
        RIGHT -> {
            newBallCoordinate.x = ballCoordinate.x + 1
        }
    }
    if (isPermittedPoint(Point(newBallCoordinate.x, newBallCoordinate.y))) {
        ballCoordinate.x = newBallCoordinate.x
        ballCoordinate.y = newBallCoordinate.y
    }
}

/*-------------------------------------------------------------------------------------------------------*/

//    @Preview
@SuppressLint("UnrememberedMutableState")
@Composable
fun Draw() {/* --------------------------- field ------------------------------ */
    val fieldWidth = 300.dp
    val fieldHeight = 1.47 * fieldWidth
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            
            ) {
            log("player1 " + player2.name + " счёт: ${player1.scope} - ${player2.scope} ")
            Text(text = "$playerScope1", color = player1.color) /* TODO */
            Text(text = "Ход: " + currentPlayerName, color = currentPlayer.color)
            Text(text = "$playerScope2", color = player2.color)
        }
        Box(
            modifier = Modifier
                .width(fieldWidth)
                .height(fieldHeight)
                .background(Green)
                .padding(10.dp),
//            horizontalArrangement = Arrangement.Center,
        ) {
            DrawField()
            DrawBallAndLine()
        }
        /* ------------------------------- keyboard ------------------------------ */
        val iconButtonSize = 50.dp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Cyan)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red),
                horizontalArrangement = Arrangement.Center
            ) {
                /* --------------------------- UP -------------------------------- */
                IconButton(
                    onClick = { ballMove(UP) },
                    modifier = Modifier.size(iconButtonSize, iconButtonSize)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_up),
                        contentDescription = "UP",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Green),
                horizontalArrangement = Arrangement.Center
            ) {
                /* --------------------------- LEFT ------------------------------ */
                IconButton(
                    onClick = { ballMove(LEFT) },
                    modifier = Modifier.size(iconButtonSize, iconButtonSize)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = "left",
                        modifier = Modifier.fillMaxSize()
                    )
                }/* --------------------------- SHOT ------------------------------ */
                IconButton(modifier = Modifier.size(iconButtonSize, iconButtonSize), onClick = {
                    shot()
//                        currentPlayer.scope++
//                        scopeText++
                    log("scope: " + playerScope1)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_shot),
                        contentDescription = "shot",
                        modifier = Modifier.fillMaxSize()
                    )
                }/* --------------------------- RIGHT ------------------------------ */
                IconButton(
                    onClick = { ballMove(RIGHT) },
                    modifier = Modifier.size(iconButtonSize, iconButtonSize)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "right",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Magenta),
                horizontalArrangement = Arrangement.Center
            ) {
                /* --------------------------- DOWN ------------------------------ */
                IconButton(
                    onClick = { ballMove(DOWN) },
                    modifier = Modifier.size(iconButtonSize, iconButtonSize)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = "ic_up",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}


/* -------------------------------------- drawing Ball and Line----------------------------------------- */
@Composable
fun DrawBallAndLine() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val cell = size.width / 8
        val lineWidth = cell / 10
//        val fix = lineWidth / 2
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
        )/* --------------------------- drawing Player ---------------------------- */
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
        val fix = lineWidth / 2
        /*        listOf(
                FieldLines(Offset(0 * cell - fix, 1 * cell), Offset(3 * cell, 1 * cell)),
                FieldLines(Offset(3 * cell, 1 * cell + fix), Offset(3 * cell, 0 * cell)),
                FieldLines(Offset(3 * cell - fix, 0 * cell), Offset(5 * cell, 0 * cell)),
                FieldLines(Offset(5 * cell, 0 * cell - fix), Offset(5 * cell, 1 * cell)),
                FieldLines(Offset(5 * cell - fix, 1 * cell), Offset(8 * cell, 1 * cell)),
                FieldLines(Offset(8 * cell, 1 * cell - fix), Offset(8 * cell, 11 * cell)),
                FieldLines(Offset(8 * cell + fix, 11 * cell), Offset(5 * cell, 11 * cell)),
                FieldLines(Offset(5 * cell, 11 * cell - fix), Offset(5 * cell, 12 * cell)),
                FieldLines(Offset(5 * cell + fix, 12 * cell), Offset(3 * cell, 12 * cell)),
                FieldLines(Offset(3 * cell, 12 * cell + fix), Offset(3 * cell, 11 * cell)),
                FieldLines(Offset(3 * cell + fix, 11 * cell), Offset(0 * cell, 11 * cell)),
                FieldLines(Offset(0 * cell, 11 * cell + fix), Offset(0 * cell, 11 * cell)),
                FieldLines(Offset(0 * cell, 11 * cell), Offset(0 * cell, 1 * cell)),
                FieldLines(Offset(0 * cell, 6 * cell), Offset(8 * cell, 6 * cell)),
            ).forEach {
                drawLine(color = Black, strokeWidth = lineWidth, start = it.start, end = it.end)
            }*/
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

/*---------------------- utilities ------------------------------*/
fun log(s: Any) {
    Log.d("MyLog", s.toString())
}