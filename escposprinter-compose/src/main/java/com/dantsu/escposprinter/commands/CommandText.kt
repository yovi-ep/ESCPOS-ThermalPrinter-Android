package com.dantsu.escposprinter.commands

import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.attributes.FontSize
import com.dantsu.escposprinter.compose.PrintAlign
import com.dantsu.escposprinter.extensions.padText
import com.dantsu.escposprinter.extensions.wrapText

/**
 * Created by yovi.putra on 15/07/22"
 * Project name: ThermalPrinter
 **/

/**
 * [CommandText] is a command to print the string data type to the printer
 */
open class CommandText(
    open val content: String,
    open val align: PrintAlign = PrintAlign.Left(),
    open val isBold: Boolean = false,
    open val size: FontSize = FontSize.NORMAL
) : PrintCommand {

    override fun parse(printer: EscPosPrinter): String = toESCText(
        content = content,
        align = align,
        isBold = isBold,
        size = size,
        maxChar = printer.printerNbrCharactersPerLine
    ) + "\n"
}