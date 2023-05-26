package ru.izotov.football

import androidx.compose.ui.graphics.Color

data class Line(
    val start: Point,
    val end: Point,
    val color: Color = Color.Black
)
