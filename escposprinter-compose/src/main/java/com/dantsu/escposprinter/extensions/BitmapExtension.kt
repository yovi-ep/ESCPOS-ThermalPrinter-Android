package com.dantsu.escposprinter.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.DisplayMetrics
import kotlin.math.ceil

/**
 * Created by yovi.putra on 15/07/22"
 * Project name: ThermalPrinter
 **/


/**
 * Make a new bitmap for canvassing,
 * with the same width and height only as pure white
 */
internal fun whiteBitmap(width: Int, height: Int, displayMetrics: DisplayMetrics? = null): Bitmap {
    val bitmap = Bitmap.createBitmap(
        displayMetrics,
        width,
        height,
        Bitmap.Config.ARGB_8888
    )

    Canvas(bitmap).drawColor(Color.WHITE)
    return bitmap
}

internal fun Bitmap.toGrayscale(): Bitmap {
    val bitmap = whiteBitmap(width, height)
    val paint = createGrayscalePaint()
    Canvas(bitmap).drawBitmap(this, 0f, 0f, paint)
    return bitmap
}

internal fun Bitmap.splitHeight(maxHeight: Int = 256): List<Bitmap> {
    val yCount = height / maxHeight
    // Allocate a array to hold the individual images.
    val bitmaps = arrayListOf<Bitmap>()

    // Loop the array and create bitmaps for each part
    for (y in 0 until yCount) {
        // Create the sliced bitmap
        val part = Bitmap.createBitmap(this, 0, y * maxHeight, width, maxHeight)
        bitmaps.add(part)
    }

    // sisa bagian
    val remainder = height % maxHeight
    if (remainder > 0) {
        val part = Bitmap.createBitmap(this, 0, yCount * maxHeight, width, remainder)
        bitmaps.add(part)
    }

    return bitmaps
}

internal fun Bitmap.rescale(maxWidth: Int): Bitmap {
    var newBitmap = this

    apply {
        if (width > maxWidth) {
            val ratio = height.toDouble() / width.toDouble()
            val newHeight = ceil(maxWidth * ratio).toInt()
            newBitmap = Bitmap.createScaledBitmap(this, maxWidth, newHeight, false)
        }
    }

    return newBitmap
}