/*
 * Project: <<projectname>>
 * Copyright (c) 2022 Xilin Jia <https://github.com/XilinJia>
 * All rights reserved.
 * This software is released under the MIT license
 * https://opensource.org/licenses/MIT
 */

package me.djia.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedButton
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun App() {
    var showFirstCanvas by remember { mutableStateOf(true) }
    var showSecondCanvas by remember { mutableStateOf(false) }

    if (wrongColumn) PlatAlertDialog("Invalid column")
    Box {
        var showPanel by remember { mutableStateOf(true) }
        var query by remember { mutableStateOf(false) }
        var zoomx by remember { mutableStateOf(1f) }
        var infoText by remember { mutableStateOf("") }

        Row(Modifier.padding(start = 5.dp))
        {
            if (showPanel) {
                Column(Modifier.padding(top = 10.dp, bottom = 10.dp))
                {
                    if (columnNames.isNotEmpty()) {
                    OutlinedButton(onClick = {
                        showFirstCanvas = !showFirstCanvas
                    }) {
                        Text("Toggle1")
                    }
                    }
                    Spacer(Modifier.weight(0.2f))
                    OutlinedButton(onClick = {
                        pickFile()
                    }) {
                        Text("File")
                    }
                    Spacer(Modifier.weight(1f))
                    if (columnNames.isNotEmpty()) {
                    OutlinedButton(onClick = {
                        showSecondCanvas = !showSecondCanvas
                    }) {
                        Text("Toggle2")
                    }
                    OutlinedButton(onClick = {
                        query = !query
                        if (query && infoText == "") infoText = "Tap on a point to see data info"
                    }) {
                        Text("?")
                    }
                    }
                }
                Divider(
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
            }
            Column(Modifier.weight(1f)) {
                var offsetX by remember { mutableStateOf(0f) }
                var canvasWidth = 0f
                var diffAve by remember { mutableStateOf(0f) }
                val strokeWidth = with(LocalDensity.current) {
                    4.dp.toPx()
                }
                if (showFirstCanvas) {
                    Box(Modifier.weight(1f))
                    {
                        var zoomy by remember { mutableStateOf(1f) }
                        var offsetY by remember { mutableStateOf(0f) }
                        Canvas(modifier = Modifier.fillMaxSize()
                            .clipToBounds()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = { offset ->
                                        val indx = ((offset.x - offsetX) / canvasWidth / zoomx * numRows).roundToInt()
                                        if (df != null && indx >= 0 && indx < df!!.rowsCount()) {
                                            infoText = df!!.get(indx).toString()
                                            if (showSecondCanvas && dataY[1].size > 0) {
                                                infoText += "\n ${
                                                    denormalizeY(
                                                        1,
                                                        dataY[1][indx]
                                                    )
                                                } with average=$diffAve"
                                            }
                                        }
                                    },
                                )
                            }
                            .pointerInput(Unit) {
                                detectTransformGestures(
                                    onGesture = { _, pan, _, _ ->
                                        offsetX += pan.x
                                        offsetY += pan.y
                                    }
                                )
                            }
                        )
                        {
                            canvasWidth = size.width
                            val canvasHeight = size.height
                            if (dataloaded > 0) {
                                val multDataPts = mutableListOf<MutableList<Offset>>()
                                multDataPts.add(mutableListOf())
                                val dataPts = multDataPts[0]
                                for (i in 0 until dataY[0].size) {
                                    val x = dataX[0][i] * canvasWidth * zoomx + offsetX
                                    val y = (1 - dataY[0][i]) * canvasHeight * zoomy + offsetY
                                    dataPts.add(Offset(x, y))
                                }
                                drawPoints(dataPts, PointMode.Points, Color.Blue, strokeWidth = strokeWidth)
                            }
                        }
                        Row {
                            Spacer(Modifier.weight(1f))
                            if (columnNames.isNotEmpty()) {
                                MakeDropDownMenu("Column", columnNames) {
                                    iChart = 0
                                    getColumn(it)
                                }
                            }
                        }
                    }
                }
                if (showSecondCanvas) {
                    Divider(color = Color.Green, modifier = Modifier.height(1.dp))
                    Box(Modifier.weight(0.5f))
                    {
                        var zoomy by remember { mutableStateOf(1f) }
                        var offsetY by remember { mutableStateOf(0f) }
                        Canvas(modifier = Modifier.fillMaxSize()
                            .clipToBounds()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = { offset ->
                                        val indx = ((offset.x - offsetX) / canvasWidth / zoomx * numRows).roundToInt()
                                        if (df != null && indx >= 0 && indx < df!!.rowsCount()) {
                                            infoText = df!!.get(indx).toString()
                                            infoText += "\n ${
                                                denormalizeY(
                                                    1,
                                                    dataY[1][indx]
                                                )
                                            } with average=$diffAve"
                                        }
                                    },
                                )
                            }
                            .pointerInput(Unit) {
                                detectTransformGestures(
                                    onGesture = { _, pan, _, _ ->
                                        offsetX += pan.x
                                        offsetY += pan.y
                                    }
                                )
                            }
                        )
                        {
                            canvasWidth = size.width
                            val canvasHeight = size.height
                            if (dataloaded > 0) {
                                val multDataPts = mutableListOf<MutableList<Offset>>()
                                multDataPts.add(mutableListOf())
                                val dataPts = multDataPts[0]
                                var yAve = 0f
                                for (i in 0 until dataY[1].size) {
                                    val x = dataX[1][i] * canvasWidth * zoomx + offsetX
                                    val y = (1 - dataY[1][i]) * canvasHeight * zoomy + offsetY
                                    yAve += y
                                    dataPts.add(Offset(x, y))
                                }
                                yAve /= dataY[1].size
                                diffAve = denormalizeY(1, (1-(yAve - offsetY)/zoomy/canvasHeight))
                                drawPoints(dataPts, PointMode.Points, Color.Gray, strokeWidth = strokeWidth)
                                drawLine(Color.Cyan, Offset(0f, yAve), Offset(canvasWidth, yAve), strokeWidth = strokeWidth)
                            }
                        }
                        Row {
                            if (columnNames.isNotEmpty()) {
                                var col1 by remember { mutableStateOf("") }
                                var col2 by remember { mutableStateOf("") }
                                MakeDropDownMenu("Col1", columnNames) { label ->
                                    iChart = 1
                                    col1 = label
                                }
                                Spacer(Modifier.width(5.dp))
                                MakeDropDownMenu("Col2", columnNames) { label ->
                                    iChart = 1
                                    col2 = label
                                }
                                Spacer(Modifier.width(5.dp))
                                var lag by remember { mutableStateOf("0") }
                                BasicTextField(
                                    value = lag,
                                    singleLine = true,
                                    modifier = Modifier.width(100.dp),
                                    onValueChange = { lag = it },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                )
                                Spacer(Modifier.weight(1f))
                                Text(
                                    modifier = Modifier.clickable(onClick = {
                                        iChart = 1
                                        getColumnsDiff(col1, col2, lag.toInt())
                                    }),
                                    text = "c1-c2[j]"
                                )
                            }
                        }
                    }
                }
                if (query) {
                    Divider()
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = infoText,
                        fontSize = 11.sp,
                    )

                }
            }
        }
        Column {
            Spacer(Modifier.weight(1f))
            OutlinedButton(onClick = {
                showPanel = !showPanel
            }) {
                Text("Panel")
            }
            Row {
                Text(
                    modifier = Modifier.width(40.dp)
                        .clickable(onClick = {
                            zoomx *= 0.9f
                        }),
                    text = "-",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.width(40.dp)
                        .clickable(onClick = {
                            zoomx *= 1.1f
                        }),
                    text = "+",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.weight(1f))
        }
    }
}