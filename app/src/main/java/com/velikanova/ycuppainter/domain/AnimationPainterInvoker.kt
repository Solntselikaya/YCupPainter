package com.velikanova.ycuppainter.domain

import com.velikanova.ycuppainter.domain.command.Command

class AnimationPainterInvoker {
    private val commands = mutableListOf<Command>()
    private val undoneCommands = mutableListOf<Command>()

    fun execute(command: Command) {
        commands.add(command)
        command.execute()

        undoneCommands.clear()
    }

    fun undo() {
        if (commands.isEmpty()) {
            return
        }

        val lastCommand = commands.removeAt(commands.lastIndex)
        lastCommand.undo()
        undoneCommands.add(lastCommand)
    }

    fun redo() {
        if (undoneCommands.isEmpty()) {
            return
        }

        val lastUndoneCommand = undoneCommands.removeAt(undoneCommands.lastIndex)
        commands.add(lastUndoneCommand)
        lastUndoneCommand.execute()
    }

    fun getHaveCommandsToUndo(): Boolean {
        return commands.isNotEmpty()
    }

    fun getHaveCommandsToRedo(): Boolean {
        return undoneCommands.isNotEmpty()
    }

    fun clear() {
        commands.clear()
        undoneCommands.clear()
    }
}