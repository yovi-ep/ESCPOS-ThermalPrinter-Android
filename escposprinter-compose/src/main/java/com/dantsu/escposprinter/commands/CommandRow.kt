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
 * [CommandRow] used when you want print per-line multiple column
 * which the length of the columns will be the same
 * @param commands columns content
 * @param verticalAlign
 */
class CommandRow(
    val commands: ArrayList<CommandText>,
    val verticalAlign: VerticalAlign = VerticalAlign.Top
) : CommandText("", PrintAlign.Left()) {

    override fun parse(printer: EscPosPrinter): String {
        val fullSpan = printer.printerNbrCharactersPerLine
        val partSpan = fullSpan / commands.size
        val matrix = extractCommands(partSpan)
        val maxRow = matrix.map { it.size }.maxOrNull() ?: 0

        normalize(matrix, partSpan, maxRow, verticalAlign)

        return sorting(matrix, maxRow)
    }

    fun writeTest(universal: CommandRow): String {
        val fullSpan = 32
        val partSpan = fullSpan / universal.commands.size
        val matrix = extractCommands(partSpan)
        val maxRow = matrix.map { it.size }.maxOrNull() ?: 0

        normalize(matrix, partSpan, maxRow, universal.verticalAlign)

        return sorting(matrix, maxRow)
    }

    /**
     * Extract command from CommandText list to String
     */
    private fun extractCommands(
        partSpan: Int,
    ): ArrayList<List<String>> {
        val column = arrayListOf<List<String>>()

        //extract all commands text
        commands.forEach { command ->
            val rowList = extractText(command, partSpan)
            column.add(rowList)
        }

        return column
    }

    /**
     * Extract Command Text with wrap and padding
     * result: ["word_line1", "word_line2"]
     */
    private fun extractText(
        command: CommandText,
        partSpan: Int
    ): ArrayList<String> {
        val column = arrayListOf<String>()

        //wrap content and create new line
        command.content.wrapText(partSpan).forEach {
            //create padding per alignment
            var text = it.trimEnd().padText(command.align, partSpan)

            if (command.isBold) {
                text = "<b>$text</b>"
            }

            column.add(text)
        }

        return column
    }

    /**
     * Normalize list item size per command
     * if per command row size < others command row, so create space to command row
     */
    private fun normalize(
        matrix: ArrayList<List<String>>,
        partSpan: Int,
        maxRow: Int,
        verticalAlign: VerticalAlign
    ) {
        val space = " ".repeat(partSpan)

        matrix.forEachIndexed { idx, it ->
            val row = it.toMutableList()

            val insertedAt = when (verticalAlign) {
                is VerticalAlign.Top -> row.size
                else -> 0
            }

            val totalBlank = maxRow - row.size
            for (i in 0 until totalBlank) {
                row.add(insertedAt, space)
            }

            matrix[idx] = row
        }
    }

    /**
     * Get data per line with same index if multiline
     */
    private fun sorting(
        matrix: ArrayList<List<String>>,
        maxRow: Int
    ): String {
        val result = arrayListOf<String>()
        val leftAlign = toESCAlign(PrintAlign.Left())

        for (rowIdx in 0 until maxRow) {
            var row = ""

            matrix.forEachIndexed { _, column ->
                row = row.plus(column.getOrNull(rowIdx).orEmpty())
            }

            if (row.trim().isNotBlank()) {
                result.add(row)
            }
        }

        return result.joinToString("\n") {
            "$leftAlign$it"
        }.plus("\n")
    }
}