package com.dantsu.escposprinter.commands

import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.compose.PrintAlign
import com.dantsu.escposprinter.compose.VerticalAlign
import com.dantsu.escposprinter.extensions.padText
import com.dantsu.escposprinter.extensions.wrapText

/**
 * Created by yovi.putra on 15/07/22"
 * Project name: ThermalPrinter
 **/

/**
 * [CommandRowSpanned] used when you want print per-line multiple column
 * which the length of the column will be determined
 * @param commands columns content
 * @param verticalAlign
 * @param padding
 */
class CommandRowSpanned(
    val commands: ArrayList<CommandTextSpanned>,
    val verticalAlign: VerticalAlign = VerticalAlign.Top,
    val padding: Int = 0
) : PrintCommand {

    private class ColumnValue(
        val spanLength: Int,
        val lines: ArrayList<String>
    )

    override fun parse(printer: EscPosPrinter): String {
        // add the horizontal padding to the printout
        val horizontalPadding = 2 * padding
        val fullSpanLength = printer.printerNbrCharactersPerLine - horizontalPadding

        val matrix = extractCommands(fullSpanLength)
        val maxRow = matrix.maxOf { it.lines.size }

        normalize(matrix, verticalAlign, maxRow)

        return sorting(matrix, maxRow, padding)
    }

    /**
     * Get all the lines for each command,
     * with it's spanLength
     */
    private fun extractCommands(
        fullSpanLength: Int
    ): ArrayList<ColumnValue> {
        val column = arrayListOf<ColumnValue>()

        val spanLengthDistribution = Span.distributeLength(
            commands,
            spanAccessor = CommandTextSpanned::span,
            fullSpanLength = fullSpanLength
        )

        for ((command, spanLength) in spanLengthDistribution) {
            val rowList = extractText(command, spanLength)
            column.add(rowList)
        }

        return column
    }

    /**
     * Fit [CommandTextSpanned.content] to [spanLength] characters space
     * according to it's alignment
     * @see [CommandTextSpanned.align]
     */
    private fun extractText(
        command: CommandTextSpanned,
        spanLength: Int
    ): ColumnValue {
        val row = arrayListOf<String>()

        // content would have multiple lines if it doesn't fit the spanLength
        val lines = command.content.wrapText(spanLength)

        for (line in lines) {

            // add empty space to the line if it is less than the spanLength
            var text = line.trimEnd().padText(command.align, spanLength)

            if (command.isBold) {
                text = "<b>$text</b>"
            }

            row.add(text)
        }

        return ColumnValue(
            spanLength = spanLength,
            lines = row
        )
    }

    /**
     * Fill [matrix] with the blank spaces
     * if the column row size is less than [maxRow]
     *
     * What part of column that would have the blank row is
     * determined by [verticalAlign]
     *
     * [VerticalAlign.Top] would make the bottom part blank
     * and [VerticalAlign.Bottom] would make the top blank
     */
    private fun normalize(
        matrix: ArrayList<ColumnValue>,
        verticalAlign: VerticalAlign,
        maxRow: Int
    ) {
        for ((idx, column) in matrix.withIndex()) {
            val row = column.lines
            val space = " ".repeat(column.spanLength)

            val insertedAt = when (verticalAlign) {
                is VerticalAlign.Top -> row.size
                else -> 0
            }

            val totalBlank = maxRow - row.size
            for (i in 0 until totalBlank) {
                row.add(insertedAt, space)
            }

            matrix[idx] = ColumnValue(
                spanLength = column.spanLength,
                lines = row
            )
        }
    }

    /**
     * Join all the columns into a string
     * by joining it's row by other column row's with the same index
     *
     * Also add padding to the printout
     *
     * with newline between each row
     */
    private fun sorting(
        matrix: ArrayList<ColumnValue>,
        maxRow: Int,
        padding: Int
    ): String {
        val result = arrayListOf<String>()
        val leftAlign = toESCAlign(PrintAlign.Left())

        val padSpace = " ".repeat(padding)

        for (rowIdx in 0 until maxRow) {
            val rowStr = StringBuilder()

            rowStr.append(padSpace)

            for (column in matrix) {
                rowStr.append(column.lines[rowIdx])
            }

            rowStr.append(padSpace)

            if (rowStr.trim().isNotBlank()) {
                result.add(rowStr.toString())
            }
        }

        return result.joinToString("\n") {
            "$leftAlign$it"
        }.plus("\n")
    }
}