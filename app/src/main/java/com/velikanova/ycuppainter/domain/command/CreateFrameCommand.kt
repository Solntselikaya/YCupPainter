package com.velikanova.ycuppainter.domain.command

import com.velikanova.ycuppainter.domain.FrameDrawing

class CreateFrameCommand(
    private val receiver: FrameDrawing,
): Command {
    override fun execute() {
        receiver.createFrame()
    }

    override fun undo() {
        receiver.deleteFrame()
    }
}