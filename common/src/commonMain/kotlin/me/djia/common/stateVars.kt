/*
 * Project: CSVAnalyser
 * Copyright (c) 2022 Xilin Jia <https://github.com/XilinJia>
 * All rights reserved.
 * This software is released under the MIT license
 * https://opensource.org/licenses/MIT
 */

package me.djia.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue

var dataloaded by mutableStateOf(0)
val columnNames = mutableStateListOf<String>()
var wrongColumn by mutableStateOf(false)
