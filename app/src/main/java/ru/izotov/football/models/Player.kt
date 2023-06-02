package ru.izotov.football.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import ru.izotov.football.controller.ballCoordinate
import ru.izotov.football.controller.currentPlayer
import ru.izotov.football.models.Point

data class Player(
    var name: String,
    val color: Color,
    var scope: Byte,
    val gate: Point,
)

