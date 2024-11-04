package com.velikanova.ycuppainter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.velikanova.ycuppainter.ui.screen.painter.AnimationProject
import com.velikanova.ycuppainter.ui.theme.YCupPainterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YCupPainterTheme {
                AnimationProject()
            }
        }
    }
}