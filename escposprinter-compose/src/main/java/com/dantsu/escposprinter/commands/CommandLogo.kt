package com.dantsu.escposprinter.commands

import android.graphics.Bitmap
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.EscPosPrinterCommands
import com.dantsu.escposprinter.compose.PrintAlign
import com.dantsu.escposprinter.extensions.rescale
import com.dantsu.escposprinter.extensions.splitHeight
import com.dantsu.escposprinter.extensions.toGrayscale
import com.dantsu.escposprinter.textparser.PrinterTextParserImg

/**
 * Created by yovi.putra on 15/07/22"
 * Project name: ThermalPrinter
 **/

/**
 * [CommandLogo] used when print company logo (256x256)
 * @param bitmap
 * @param align
 */
open class CommandLogo(
    val bitmap: Bitmap,
    val align: PrintAlign = PrintAlign.Center()
) : PrintCommand {
    override fun parse(printer: EscPosPrinter): String {
        // convert internal align to escposprinter alignment
        val escAlign = toESCAlign(align)
        // convert bitmap to grayscale
        val newBitmap = bitmap.toGrayscale()
        // rescale bitmap width to 256px
        val imageFixed = newBitmap.rescale(256)

        // storage temporary for store bitmap part
        var commands = ""
        // the bitmap will be cut in parts to reduce the printer buffer process
        imageFixed.splitHeight().forEach {
            val imageBytes = EscPosPrinterCommands.bitmapToBytes(it)
            val imageHex = PrinterTextParserImg.bytesToHexadecimalString(imageBytes)

            //double \n https://github.com/DantSu/ESCPOS-ThermalPrinter-Android/issues/220
            commands += "$escAlign<img>$imageHex</img>\n"
        }

        commands += "\n"

        return commands
    }

}