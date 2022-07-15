package com.dantsu.escposprinter.extensions

import com.dantsu.escposprinter.compose.PrintAlign

/**
 * Created by yovi.putra on 15/07/22"
 * Project name: ThermalPrinter
 **/


/**
 * move text to new line if the text over limit
 */
internal fun String.wrapText(maxLength: Int): List<String> {
    val result = ArrayList<String>()
    var buffer = this

    while (buffer.isNotEmpty()) {

        if (buffer.length > maxLength) {
            var breakPoint = buffer.lastIndexOf(' ', maxLength)

            if (breakPoint == -1) {
                breakPoint = buffer.lastIndexOf('.', maxLength)
            }
            if (breakPoint == -1) {
                breakPoint = buffer.lastIndexOf(',', maxLength)
            }
            if (breakPoint == -1) {
                breakPoint = maxLength
            }

            result.add(buffer.take(breakPoint))

            buffer = buffer.substring(breakPoint).trimStart()
        } else {
            result.add(buffer)
            break
        }
    }

    return result
}

/**
 * adjust full text on one line according to align
 */
internal fun String.padText(align: PrintAlign, span: Int): String {
    if (span <= 0) return this.take(span)

    val padding = " ".repeat(span)
    if (this.isBlank()) return padding

    return when (align) {
        is PrintAlign.Left -> padEnd(span).take(span)
        is PrintAlign.Right -> padStart(span).takeLast(span)
        is PrintAlign.Center -> {
            val padLength = span - length
            val startLength = padLength / 2
            padStart(span - startLength).padEnd(span).take(span)
        }
    }
}