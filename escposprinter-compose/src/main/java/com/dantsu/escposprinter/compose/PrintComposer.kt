package com.dantsu.escposprinter.compose

import android.graphics.Bitmap
import com.dantsu.escposprinter.attributes.FontSize
import com.dantsu.escposprinter.commands.*

/**
 * Created by yovi.putra on 14/07/22"
 * Project name: ThermalPrinter
 **/

abstract class PrintComposer : PrintCompose() {

    /**
     * Add command
     */
    fun add(command: PrintCommand) {
        commands.add(command)
    }

    /**
     * Add multiple command
     */
    fun addAll(command: List<PrintCommand>) {
        commands.addAll(command)
    }

    /**
     * Add text to line
     */
    fun addText(
        content: String,
        align: PrintAlign = PrintAlign.Left(),
        isBold: Boolean = false,
        size: FontSize = FontSize.NORMAL
    ): CommandText {
        val text = CommandText(content = content, align = align, isBold = isBold, size = size)
        commands.add(text)
        return text
    }

    /**
     * Add text to line
     */
    fun addText(
        content: String,
        padding: Int,
        align: PrintAlign = PrintAlign.Left(),
        isBold: Boolean = false,
        size: FontSize = FontSize.NORMAL
    ): CommandRowSpanned {
        val text = CommandTextSpanned(
            content = content,
            span = Span.FullSpanned,
            align = align,
            isBold = isBold,
            fontSize = size
        )
        val column = CommandRowSpanned(
            commands = arrayListOf(text),
            padding = padding
        )
        commands.add(column)
        return column
    }

    /**
     * text to line
     */
    fun text(
        content: String,
        align: PrintAlign = PrintAlign.Left(),
        isBold: Boolean = false,
        size: FontSize = FontSize.NORMAL
    ): CommandText = CommandText(content = content, align = align, isBold = isBold, size = size)

    fun text(
        content: String,
        span: Span,
        align: PrintAlign = PrintAlign.Left(),
        isBold: Boolean = false,
        size: FontSize = FontSize.NORMAL
    ): CommandTextSpanned = CommandTextSpanned(content, isBold, span, align, size)

    /**
     * Add Title to line
     */
    fun addTitle(
        content: String,
        isBold: Boolean = true
    ): CommandTitle {
        val title = CommandTitle(content = content, isBold = isBold)
        commands.add(title)
        return title
    }

    /**
     * Add multiple columns to line
     */
    fun addRow(
        vararg columns: CommandText,
        verticalAlign: VerticalAlign = VerticalAlign.Top
    ) {
        commands.add(
            CommandRow(
                commands = arrayListOf(*columns),
                verticalAlign = verticalAlign
            )
        )
    }

    /**
     * Add multiple columns to line for [text]
     */
    fun addRow(
        vararg columns: CommandTextSpanned,
        verticalAlign: VerticalAlign = VerticalAlign.Top,
        padding: Int = 0
    ) {
        commands.add(
            CommandRowSpanned(
                commands = arrayListOf(*columns),
                verticalAlign = verticalAlign,
                padding = padding
            )
        )
    }

    /**
     * Add blank to line
     */
    fun addBlank(): CommandBlank {
        val blank = CommandBlank()
        commands.add(blank)
        return blank
    }

    /**
     * Add divider to line
     */
    fun addDivider(
        content: String = "-",
        padding: Int = 0
    ): CommandDivider {
        val divider = CommandDivider(content = content, padding = padding)
        commands.add(divider)
        return divider
    }

    /**
     * Add logo
     */
    fun addLogo(
        bitmap: Bitmap,
        align: PrintAlign = PrintAlign.Center()
    ): CommandLogo {
        val image = CommandLogo(bitmap = bitmap, align = align)
        commands.add(image)
        return image
    }

    /**
     * add full image only
     */
    fun addFullImage(
        bitmap: Bitmap,
        align: PrintAlign = PrintAlign.Center()
    ): CommandFullImage {
        val image = CommandFullImage(bitmap = bitmap, align = align)
        commands.add(image)
        return image
    }

    /**
     * add feed paper
     */
    fun addFeedPaper(maxLines: Int = 3): CommandBlank {
        val feedPaper = CommandBlank(maxLines)
        commands.add(feedPaper)
        return feedPaper
    }
}