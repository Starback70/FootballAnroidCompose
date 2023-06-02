package ru.izotov.football.models

import androidx.compose.ui.graphics.Color
import ru.izotov.football.ui.theme.Black

data class Line(
    val start: Point,
    val end: Point,
    val color: Color = Black
)
