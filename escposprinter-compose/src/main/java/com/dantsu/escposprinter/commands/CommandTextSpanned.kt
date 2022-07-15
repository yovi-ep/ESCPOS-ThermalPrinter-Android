package com.dantsu.escposprinter.commands

import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.attributes.FontSize
import com.dantsu.escposprinter.compose.PrintAlign

/**
 * Created by yovi.putra on 15/07/22"
 * Project name: ThermalPrinter
 **/

/**
 * [CommandRowSpanned] used when you want print string data type
 * which the length of the column will be determined
 * @param commands columns content
 * @param verticalAlign
 * @param padding
 */
class CommandTextSpanned(
    val content: String,
    val isBold: Boolean = false,
    val span: Span = Span.FullSpanned,
    val align: PrintAlign = PrintAlign.Left(),
    val fontSize: FontSize = FontSize.NORMAL
) : PrintCommand {

    override fun parse(printer: EscPosPrinter): String {
        val fullSpanLength = printer.printerNbrCharactersPerLine
        val spanLength = span.length(fullSpanLength, cut = true)
        return toESCText(
            content = content,
            align = align,
            isBold = isBold,
            size = fontSize,
            maxChar = spanLength
        ) + "\n"
    }
}
