package com.example.sudokusolver

import android.view.View

interface Solver {
    fun solve(view: View): Boolean

    fun insertNumber(number: Int)

    fun clearNumber()

    fun reset()

    fun getBoard(): Array<IntArray>

    fun getSelectedValue(row: Int, column: Int): Int

    fun getSelection(): Pair<Int, Int>

    fun getEmptyCellsPositions(): List<Pair<Int, Int>>

    fun setSelection(row: Int, column: Int)

    fun isBoardValid(row: Int, column: Int, number: Int): Boolean

    fun isRowValid(row: Int, number: Int): Boolean

    fun isColumnValid(column: Int, number: Int): Boolean

    fun isBoxValid(row: Int, column: Int, number: Int): Boolean

    fun isUserInserted(row: Int, column: Int): Boolean

    fun isSelected(row: Int, column: Int): Boolean

    fun isNotEmpty(row: Int, column: Int): Boolean

    fun isSelectedBox(row: Int, column: Int): Boolean

}