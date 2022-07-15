package com.dantsu.escposprinter.compose

import android.graphics.Paint

/**
 * Created by yovi.putra on 14/07/22"
 * Project name: ThermalPrinter
 **/

sealed interface PrintAlign {

    class Left(
        val marginLeft: Int = 0,
        val marginRight: Int = 0
    ) : PrintAlign

    class Right(
        val marginLeft: Int = 0,
        val marginRight: Int = 0
    ) : PrintAlign

    class Center(
        val marginLeft: Int = 0,
        val marginRight: Int = 0
    ) : PrintAlign
}

sealed class VerticalAlign {
    object Top : VerticalAlign()
    object Bottom : VerticalAlign()
}

fun PrintAlign.toPaintAlign(): Paint.Align {
    return when (this) {
        is PrintAlign.Right -> Paint.Align.RIGHT
        is PrintAlign.Center -> Paint.Align.CENTER
        else -> Paint.Align.LEFT
    }
}