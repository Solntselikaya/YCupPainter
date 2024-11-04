package com.velikanova.ycuppainter.domain.command

import androidx.compose.ui.graphics.Path
import com.velikanova.ycuppainter.domain.FrameDrawing
import com.velikanova.ycuppainter.ui.screen.project.painter.PathProps

class DeleteFrameCommand(
    private val receiver: FrameDrawing,
): Command {
    override fun execute() {
        receiver.deleteFrame()
    }

    override fun undo() {
        receiver.createFrame()
    }
}