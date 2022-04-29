/*
 * Project: CSVAnalyser
 * Copyright (c) 2022 Xilin Jia <https://github.com/XilinJia>
 * All rights reserved.
 * This software is released under the MIT license
 * https://opensource.org/licenses/MIT
 */

package me.djia.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import moe.tlaster.kfilepicker.PlatformFile
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

actual fun fileInputStreamer(file: PlatformFile): InputStream? {
    return FileInputStream(File(file.path))
}

@Composable
actual fun MakeDropDownMenu(
    text :String, menuItems: List<String>, cb: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var buttonText by remember { mutableStateOf(text) }

    Box {
        Row(Modifier.clickable {
            expanded = !expanded
        }) {
            Text(buttonText)
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            menuItems.forEach { label ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    buttonText = label
                    cb(label)
                }) {
                    Text(text = label)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
actual fun PlatAlertDialog(message :String) {
    AlertDialog(
        text = {
            Text(message)
        },
        onDismissRequest = { wrongColumn = false},
        confirmButton = {
            TextButton(onClick = {
                wrongColumn = false
            })
            { Text(text = "OK") }
        },
    )
}

