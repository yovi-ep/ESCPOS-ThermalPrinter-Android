package com.dantsu.escposprinter.commands

import com.dantsu.escposprinter.attributes.FontSize
import com.dantsu.escposprinter.compose.PrintAlign

/**
 * Created by yovi.putra on 15/07/22"
 * Project name: ThermalPrinter
 **/

/**
 * [CommandBlank] is a command to print the new line
 * @param maxLines default is one line
 */
open class CommandBlank(
    val maxLines: Int = 1
) : CommandText("\n", PrintAlign.Left(), false, FontSize.NORMAL)