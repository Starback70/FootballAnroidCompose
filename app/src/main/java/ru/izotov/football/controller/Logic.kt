package ru.izotov.football.controller

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import ru.izotov.football.models.Direction
import ru.izotov.football.models.Line
import ru.izotov.football.models.Parameters
import ru.izotov.football.models.Player
import ru.izotov.football.models.Point
import ru.izotov.football.ui.theme.Blue
import ru.izotov.football.ui.theme.Red
import kotlin.math.abs


val FIELD_WIDTH = 320.dp

val params = Parameters()
var lineList = mutableListOf<Line>()
var pointList = mutableListOf(Point(params.centerOfField.x, params.centerOfField.y))
var ballCoordinate = Point(params.centerOfField.x, params.centerOfField.y)
var player1 = Player("Красный", Red, 0, params.gateOfPlayer1)
var player2 = Player("Синий", Blue, 0, params.gateOfPlayer2)
var currentPlayer = player1
var currentPlayerName by mutableStateOf(currentPlayer.name)
var playerScope1 by mutableStateOf(player1.scope)
var playerScope2 by mutableStateOf(player2.scope)


class Logic(private val context: Context) {
    fun shot() {
        val lastCoordinate = pointList.last()
        val x1 = lastCoordinate.x
        val y1 = lastCoordinate.y
        val x2 = ballCoordinate.x
        val y2 = ballCoordinate.y
        val line = Line(Point(x1, y1), Point(x2, y2), color = currentPlayer.color)
        if (isShotPermitted(lastCoordinate, line)) {
            checkPlayerChange()
            lineList.add(line)
            pointList.add(Point(x2, y2))
            checkGoal()
        } else {
            Toast.makeText(context, "Такой ход не возможен", Toast.LENGTH_SHORT).show()
        }
        log("Params().fieldPoints: " + params.fieldPoints)
        log("lineList.size: " + lineList.size)
        log("lineList: " + lineList)
    }
    
    private fun checkGoal() {
        when (ballCoordinate) {
            player1.gate -> {
                ++player2.scope
                Toast.makeText(context, "${player2.name} забивает гол!", Toast.LENGTH_LONG).show()
                log("Гол в ворота " + player1.name + " счёт: ${player1.scope} - ${player2.scope} ")
                newRound()
            }
            player2.gate -> {
                ++player1.scope
                Toast.makeText(context, "${player1.name} забивает гол!", Toast.LENGTH_LONG).show()
                log("Гол в ворота " + player2.name + " счёт: ${player1.scope} - ${player2.scope} ")
                newRound()
            }
        }
        playerScope1 = player1.scope
        playerScope2 = player2.scope
    }
  
    private fun checkPlayerChange() {
        val currentBallPosition = Point(ballCoordinate.x, ballCoordinate.y)
        if (!pointList.contains(currentBallPosition) && !params.borderPoints.contains(currentBallPosition)) {
            changePlayer()
        }
    }
    
    private fun changePlayer() {
        currentPlayer = if (currentPlayer === player1) {
            player2
        } else {
            player1
        }
        currentPlayerName = currentPlayer.name
    }
    
    private fun newRound() {
        lineList.clear()
        pointList.clear()
        pointList.add(Point(params.centerOfField.x, params.centerOfField.y))
        ballCoordinate.x = params.centerOfField.x
        ballCoordinate.y = params.centerOfField.y
    }
    
    fun newGame() {
       newRound()
         lineList = mutableListOf<Line>()
         pointList = mutableListOf(Point(params.centerOfField.x, params.centerOfField.y))
         ballCoordinate = Point(params.centerOfField.x, params.centerOfField.y)
         player1 = Player("Красный", Red, 0, params.gateOfPlayer1)
         player2 = Player("Синий", Blue, 0, params.gateOfPlayer2)
         currentPlayer = player1
         currentPlayerName = currentPlayer.name
         playerScope1 = player1.scope
         playerScope2 = player2.scope
    }
    
    private fun lineListNotContainsSameLine(line: Line): Boolean {
        lineList.forEach {
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
    
    private fun isSameLine(l1: Line, l2: Line): Boolean {
        return (l1.start.x == l2.start.x && l1.start.y == l2.start.y && l1.end.x == l2.end.x && l1.end.y == l2.end.y)
                || (l1.start.x == l2.end.x && l1.start.y == l2.end.y && l1.end.x == l2.start.x && l1.end.y == l2.start.y)
    }
    
    private fun isShotPermitted(lastCoordinate: Point, line: Line): Boolean {
        val x = abs(lastCoordinate.x - ballCoordinate.x)
        val y = abs(lastCoordinate.y - ballCoordinate.y)
        return ((x == 1 && y == 1) ||(x == 1 && y == 0) ||(x == 0 && y == 1))
                && (lineListNotContainsSameLine(line))
    }
    
    private fun isPointPermitted(point: Point): Boolean {
        return params.fieldPoints.contains(point)
    }
    
    fun ballMove(direction: Direction) {
        val newBallCoordinate = Point(ballCoordinate.x, ballCoordinate.y)
        when (direction) {
            Direction.UP -> {
                newBallCoordinate.y = ballCoordinate.y - 1
            }
            
            Direction.DOWN -> {
                newBallCoordinate.y = ballCoordinate.y + 1
            }
            
            Direction.LEFT -> {
                newBallCoordinate.x = ballCoordinate.x - 1
            }
            
            Direction.RIGHT -> {
                newBallCoordinate.x = ballCoordinate.x + 1
            }
        }
        if (isPointPermitted(Point(newBallCoordinate.x, newBallCoordinate.y))) {
            ballCoordinate.x = newBallCoordinate.x
            ballCoordinate.y = newBallCoordinate.y
        }
    }
}

/*---------------------- utilities ------------------------------*/
fun log(s: Any) {
    Log.d("MyLog", s.toString())
}