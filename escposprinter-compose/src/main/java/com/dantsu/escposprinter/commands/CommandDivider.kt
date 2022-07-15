package com.dantsu.escposprinter.commands

import com.dantsu.escposprinter.attributes.FontSize
import com.dantsu.escposprinter.compose.PrintAlign

/**
 * Created by yovi.putra on 15/07/22"
 * Project name: ThermalPrinter
 **/

/**
 * [CommandBlank] is a command to print the new line
 * @param content default is strip (-)
 * @param padding default is 0, set distance between content and cover page
 */
class CommandDivider(
    override val content: String = "-",
    private val padding: Int = 0
) : CommandText(
    content = content,
    align = PrintAlign.Left(marginLeft = padding, marginRight = padding),
    isBold = false,
    size = FontSize.NORMAL
)