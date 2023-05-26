package ru.izotov.football

import androidx.compose.ui.geometry.Offset

data class Point(
    var x: Int,
    var y: Int
) {
    fun toOffset(): Offset {
        return Offset(x.toFloat(), y.toFloat())
    }
    fun toOffsetMultiplyByCell(): Offset {
        return Offset(x.toFloat(), y.toFloat())
    }
}
