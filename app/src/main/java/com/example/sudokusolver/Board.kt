package com.example.sudokusolver

import android.graphics.Canvas

interface Board {
    fun extractAttributes()

    fun drawBoard(canvas: Canvas)

    fun drawGrid(canvas: Canvas)

    fun drawVerticalLines(canvas: Canvas, index: Int)

    fun drawHorizontalLines(canvas: Canvas, index: Int)

    fun drawNumbers(canvas: Canvas)

    fun drawSelectedCell(canvas: Canvas, selectedRow: Int, selectedColumn: Int)

    fun drawHighlightedRowAndColumn(canvas: Canvas, selectedRow: Int, selectedColumn: Int)

    fun drawHighlightBox(canvas: Canvas, selectedRow: Int, selectedColumn: Int)

    fun drawHighlightedCell(canvas: Canvas, line: Int, index: Int)

    fun setBoardProperties()

    fun setBoxProperties()

    fun setCellProperties()

    fun setSelectedCellProperties()

    fun setHighlightCellsProperties()

    fun setTextProperties(row: Int, column: Int)

    fun drawNumber(canvas: Canvas, row: Int, column: Int)

}