package com.dantsu.escposprinter.compose

import com.dantsu.escposprinter.commands.PrintCommand

/**
 * Created by yovi.putra on 15/07/22"
 * Project name: ThermalPrinter
 **/


abstract class PrintCompose : PrintComposeInterface {

    protected val commands = arrayListOf<PrintCommand>()

    override fun build(): List<PrintCommand> {
        compose()
        return commands
    }

    protected abstract fun compose()
}