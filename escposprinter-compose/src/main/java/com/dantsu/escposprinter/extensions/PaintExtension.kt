package com.dantsu.escposprinter.extensions

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

/**
 * Created by yovi.putra on 15/07/22"
 * Project name: ThermalPrinter
 **/

fun createGrayscalePaint(): Paint {
    val matrix = ColorMatrix().apply {
        setSaturation(0f)
    }

    val filter = ColorMatrixColorFilter(matrix)
    return Paint().apply {
        colorFilter = filter
    }
}