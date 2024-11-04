package com.velikanova.ycuppainter.domain.command

import androidx.compose.ui.graphics.Path
import com.velikanova.ycuppainter.domain.FrameDrawing
import com.velikanova.ycuppainter.ui.screen.project.painter.PathProps

class DrawLineCommand(
    private val receiver: FrameDrawing,
    private val path: Path,
    private val props: PathProps
): Command {
    override fun execute() {
        receiver.drawLine(path to props)
    }

    override fun undo() {
        receiver.removeLine(path)
    }
}