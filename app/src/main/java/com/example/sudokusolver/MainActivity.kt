package com.example.sudokusolver

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.sudokusolver.view.BoardView

class MainActivity : AppCompatActivity() {
    private val board by lazy { findViewById<BoardView>(R.id.boardView) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onNumberClick(view: View) {
        val button = view as Button
        val number = button.text.toString().toInt()
        board.solver.insertNumber(number)
        board.invalidate()
    }

    fun onClearClick(view: View) {
        board.solver.clearNumber()
        board.invalidate()
    }

    fun onSolveClick(view: View) {
        board.solver.getEmptyCellsPositions()
        Thread {
            board.solver.solve(board)
            runOnUiThread {
                board.invalidate()
            }
        }.start()
    }

    fun onResetClick(view: View) {
        board.solver.reset()
        board.invalidate()
    }

}