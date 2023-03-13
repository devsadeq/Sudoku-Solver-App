package com.example.sudokusolver

import android.view.View

interface Solver {
    fun solve(view: View): Boolean

    fun insertNumber(number: Int)

    fun clearNumber()

    fun reset()

    fun getBoard(): Array<IntArray>

    fun getEmptyCellsPositions(): List<Pair<Int, Int>>

    fun isBoardValid(row: Int, column: Int, number: Int): Boolean

    fun isRowValid(row: Int, number: Int): Boolean

    fun isColumnValid(column: Int, number: Int): Boolean

    fun isBoxValid(row: Int, column: Int, number: Int): Boolean

    fun isUserInserted(row: Int, column: Int): Boolean

}