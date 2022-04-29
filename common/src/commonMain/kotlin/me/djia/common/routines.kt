/*
 * Project: CSVAnalyser
 * Copyright (c) 2022 Xilin Jia <https://github.com/XilinJia>
 * All rights reserved.
 * This software is released under the MIT license
 * https://opensource.org/licenses/MIT
 */


package me.djia.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.kfilepicker.FilePicker
import moe.tlaster.kfilepicker.PlatformFile
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.column
import org.jetbrains.kotlinx.dataframe.api.isNumber
import org.jetbrains.kotlinx.dataframe.api.max
import org.jetbrains.kotlinx.dataframe.api.min
import org.jetbrains.kotlinx.dataframe.io.readDelim
import java.io.BufferedReader
import java.io.InputStreamReader

val dataX = mutableListOf(mutableListOf(), mutableListOf<Float>())
val dataY = mutableListOf(mutableListOf(), mutableListOf<Float>())

fun pickFile() {
    CoroutineScope(Dispatchers.Default).launch {
        val files = FilePicker.pickFiles(
            allowedExtensions = listOf("csv"),
            allowMultiple = false,
        )
        openFile(files)
    }
}

var csvFiles : List<PlatformFile>? = null

fun openFile(files :  List<PlatformFile>) {
    if (files.isEmpty()) return
    csvFiles = files
    println("file picked: " + files[0].path)
    loadDataFrame()
}

var maxY = mutableListOf(-1e10f, -1e10f)
var minY = mutableListOf(1e10f, 1e10f)
var numRows = 0
var iChart = 0

private fun normalize() {
    dataX[iChart].clear()
    numRows = dataY[iChart].size
    for (i in 0 until numRows) {
        dataX[iChart].add(i/numRows.toFloat())
        dataY[iChart][i] = (dataY[iChart][i] - minY[iChart]) / (maxY[iChart] - minY[iChart])
    }
    dataloaded++
}

fun denormalizeY(iChart :Int, y : Float): Float {
    return (maxY[iChart] - minY[iChart]) * y + minY[iChart]
}

var df : DataFrame<*>? = null

fun loadDataFrame() {
    val reader = BufferedReader(InputStreamReader(fileInputStreamer(csvFiles!![0])!!, Charsets.UTF_8))
    df = DataFrame.readDelim(reader)
    reader.close()
    columnNames.addAll(df!!.columnNames())
}

fun getColumn(_col : String) {
    if (df == null) return

    val col by column<Float>(_col)
    val values = df!!.getColumnOrNull { col }
    if (values == null || !values.isNumber()) {
        wrongColumn = true
        return
    }

    dataY[iChart].clear()
    maxY[iChart] = values.max()
    minY[iChart] = values.min()
    dataY[iChart].addAll(values.toList())
    normalize()
}

fun getColumnsDiff(_col1 : String, _col2 : String, lag : Int) {
    val col1 by column<Float>(_col1)
    val values1 = df!!.getColumnOrNull { col1 }
    if (values1 == null || !values1.isNumber()) {
        wrongColumn = true
        return
    }

    val col2 by column<Float>(_col2)
    val values2 = df!!.getColumnOrNull { col2 }
    if (values2 == null || !values2.isNumber()) {
        wrongColumn = true
        return
    }
    val values = values1.toList().toMutableList()
    maxY[iChart] = -1e10f
    minY[iChart] = 1e10f
    for (i in 0 until values.size) {
        if (i<lag) values[i] = 0f
        else values[i] -= values2[i-lag]
        if (values[i] > maxY[iChart]) maxY[iChart] = values[i]
        if (values[i] < minY[iChart]) minY[iChart] = values[i]
    }
    dataY[iChart].clear()
    dataY[iChart].addAll(values)
    normalize()
}