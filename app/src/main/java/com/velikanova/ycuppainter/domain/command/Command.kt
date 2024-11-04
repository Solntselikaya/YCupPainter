package com.velikanova.ycuppainter.domain.command

interface Command {
    fun execute()
    fun undo()
}