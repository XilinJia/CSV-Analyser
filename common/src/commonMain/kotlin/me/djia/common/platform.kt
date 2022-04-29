/*
 * Project: CSVAnalyser
 * Copyright (c) 2022 Xilin Jia <https://github.com/XilinJia>
 * All rights reserved.
 * This software is released under the MIT license
 * https://opensource.org/licenses/MIT
 */

package me.djia.common

import androidx.compose.runtime.Composable
import moe.tlaster.kfilepicker.PlatformFile
import java.io.InputStream

expect fun fileInputStreamer(file: PlatformFile): InputStream?

@Composable
expect fun MakeDropDownMenu(text :String, menuItems: List<String>, cb: (String) -> Unit)

@Composable
expect fun PlatAlertDialog(message :String)