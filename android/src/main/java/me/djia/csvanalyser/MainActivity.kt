/*
 * Project: CSVAnalyser
 * Copyright (c) 2022 Xilin Jia <https://github.com/XilinJia>
 * All rights reserved.
 * This software is released under the MIT license
 * https://opensource.org/licenses/MIT
 */

package me.djia.csvanalyser

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import me.djia.common.App
import me.djia.common.context
import moe.tlaster.kfilepicker.FilePicker

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FilePicker.init(activityResultRegistry, this, contentResolver)
        context = applicationContext
        setContent {
            MaterialTheme {
                App()
            }
        }
    }
}