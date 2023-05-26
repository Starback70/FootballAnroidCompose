package ru.izotov.football

import androidx.compose.ui.graphics.Color

data class Player(
    val name: String,
    val color: Color,
    var scope: Byte,
    val gate: Point,
)