package com.dantsu.escposprinter.commands

import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.attributes.FontSize
import com.dantsu.escposprinter.compose.PrintAlign
import com.dantsu.escposprinter.extensions.padText
import com.dantsu.escposprinter.extensions.wrapText

/**
 * Created by yovi.putra on 14/07/22"
 * Project name: ThermalPrinter
 **/

/**
 * PrintShare is an command interface to many types of print commands
 * the print command has a parse function which always returns a string
 * because [EscPosPrinter] must process string data type
 */
interface PrintCommand {

    /**
     * Parse data according to derived command
     */
    fun parse(printer: EscPosPrinter): String

    /**
     * toESCAlign should be convert internal align type to escposprinter-core type
     * @param align
     * @return String
     */
    fun toESCAlign(align: PrintAlign): String = when (align) {
        is PrintAlign.Right -> "[R]"
        is PrintAlign.Center -> "[C]"
        else -> "[L]"
    }

    /**
     * toESCText will format strings that are longer than paper length into multiline strings
     * @param content
     * @param align
     * @param isBold
     * @param size, unused
     * @param maxChar, max char per line
     * @return String
     */
    fun toESCText(
        content: String,
        align: PrintAlign,
        isBold: Boolean,
        @Suppress("UNUSED_PARAMETER") size: FontSize,
        maxChar: Int
    ): String {
        var result = ""
        val leftAlign = toESCAlign(PrintAlign.Left())

        content.wrapText(maxChar).forEach {
            val value = it.trimEnd().padText(align, maxChar)

            result.apply {
                result += if (isBold) {
                    "$leftAlign<b>$value</b>\n"
                } else {
                    "$leftAlign$value"
                }
            }
        }

        return result.trimEnd { c -> c == '\n' }
    }
}