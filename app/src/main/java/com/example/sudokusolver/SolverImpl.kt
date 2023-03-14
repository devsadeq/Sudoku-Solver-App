package com.example.sudokusolver

import android.view.View
import com.example.sudokusolver.Constants.BOARD_SIZE
import com.example.sudokusolver.Constants.BOX_SIZE
import com.example.sudokusolver.Constants.EMPTY_CELL
import com.example.sudokusolver.Constants.INITIAL_SELECTION
import com.example.sudokusolver.Constants.MAX_VALUE
import com.example.sudokusolver.Constants.MIN_VALUE

class SolverImpl : Solver {
    private val board: Array<IntArray> = Array(BOARD_SIZE) { IntArray(BOARD_SIZE) { EMPTY_CELL } }
    private val emptyCellsPositions: MutableList<Pair<Int, Int>> = mutableListOf()
    private val userInsertedNumbers: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
    private var selectedRow = INITIAL_SELECTION
    private var selectedColumn = INITIAL_SELECTION

    override fun solve(view: View): Boolean {
        if (emptyCellsPositions.isEmpty()) return true
        val (row, column) = emptyCellsPositions[0]
        for (number in MIN_VALUE..MAX_VALUE) {
            if (isBoardValid(row, column, number)) {
                board[row][column] = number
                emptyCellsPositions.removeAt(0)
                view.invalidate()
                if (solve(view)) return true
                board[row][column] = EMPTY_CELL
                emptyCellsPositions.add(0, Pair(row, column))
                view.invalidate()
            }
        }
        return false
    }

    override fun insertNumber(number: Int) {
        if (selectedRow != INITIAL_SELECTION && selectedColumn != INITIAL_SELECTION) {
            board[selectedRow][selectedColumn] = number
            userInsertedNumbers[Pair(selectedRow, selectedColumn)] = number
        }
    }

    override fun clearNumber() {
        if (selectedRow != INITIAL_SELECTION && selectedColumn != INITIAL_SELECTION) {
            board[selectedRow][selectedColumn] = EMPTY_CELL
            userInsertedNumbers.remove(Pair(selectedRow, selectedColumn))
        }
    }

    override fun reset() {
        for (row in 0 until BOARD_SIZE) {
            for (column in 0 until BOARD_SIZE) {
                board[row][column] = EMPTY_CELL
            }
        }
        emptyCellsPositions.clear()
        userInsertedNumbers.clear()
        selectedRow = INITIAL_SELECTION
        selectedColumn = INITIAL_SELECTION
    }

    override fun getBoard(): Array<IntArray> = board

    override fun getEmptyCellsPositions(): List<Pair<Int, Int>> {
        emptyCellsPositions.clear()
        for (row in 0 until BOARD_SIZE) {
            for (column in 0 until BOARD_SIZE) {
                if (board[row][column] == EMPTY_CELL) {
                    emptyCellsPositions.add(Pair(row, column))
                }
            }
        }
        return emptyCellsPositions
    }

    override fun getSelectedValue(row: Int, column: Int): Int {
        return board[row][column]
    }

    override fun getSelection(): Pair<Int, Int> {
        return Pair(selectedRow, selectedColumn)
    }

    override fun setSelection(row: Int, column: Int) {
        selectedRow = row
        selectedColumn = column
    }

    override fun isBoardValid(row: Int, column: Int, number: Int): Boolean {
        return isRowValid(row, number)
                && isColumnValid(column, number)
                && isBoxValid(row, column, number)
    }

    override fun isRowValid(row: Int, number: Int): Boolean {
        return !board[row].contains(number)
    }

    override fun isColumnValid(column: Int, number: Int): Boolean {
        return !board.map { it[column] }.contains(number)
    }

    override fun isBoxValid(row: Int, column: Int, number: Int): Boolean {
        val boxRow = row / BOX_SIZE
        val boxColumn = column / BOX_SIZE
        for (i in boxRow * BOX_SIZE until boxRow * BOX_SIZE + BOX_SIZE) {
            for (j in boxColumn * BOX_SIZE until boxColumn * BOX_SIZE + BOX_SIZE) {
                if (board[i][j] == number) return false
            }
        }
        return true
    }

    override fun isUserInserted(row: Int, column: Int): Boolean {
        return userInsertedNumbers.containsKey(Pair(row, column))
    }

    override fun isSelected(row: Int, column: Int): Boolean {
        return selectedRow == row && selectedColumn == column
    }

    override fun isNotEmpty(row: Int, column: Int): Boolean {
        return board[row][column] != EMPTY_CELL
    }

    override fun isSelectedBox(row: Int, column: Int): Boolean {
        return row != selectedRow % BOX_SIZE || column != selectedColumn % BOX_SIZE
    }

}