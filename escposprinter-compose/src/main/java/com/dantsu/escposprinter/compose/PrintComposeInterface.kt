package com.dantsu.escposprinter.compose

import com.dantsu.escposprinter.commands.PrintCommand
import java.util.Collections.emptyList

/**
 * Created by yovi.putra on 14/07/22"
 * Project name: ThermalPrinter
 **/

interface PrintComposeInterface {

    // build page content to print
    fun build(): List<PrintCommand>

    companion object {
        // page content is empty
        val empty = object : PrintComposeInterface {
            override fun build(): List<PrintCommand> = emptyList()
        }
    }
}