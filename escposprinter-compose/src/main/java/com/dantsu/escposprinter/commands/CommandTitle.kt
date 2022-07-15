package com.dantsu.escposprinter.commands

import com.dantsu.escposprinter.attributes.FontSize
import com.dantsu.escposprinter.compose.PrintAlign

/**
 * [CommandTitle] is a command text with style is bold and center alignment
 */
class CommandTitle(
    override val content: String,
    override val isBold: Boolean = true
) : CommandText(content, PrintAlign.Center(), isBold, FontSize.NORMAL)