/*
 * Project: CSVAnalyser
 * Copyright (c) 2022 Xilin Jia <https://github.com/XilinJia>
 * All rights reserved.
 * This software is released under the MIT license
 * https://opensource.org/licenses/MIT
 */

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.djia.common.App
import moe.tlaster.kfilepicker.FilePicker

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        FilePicker.init(window)
        MaterialTheme {
            App()
        }
    }
}