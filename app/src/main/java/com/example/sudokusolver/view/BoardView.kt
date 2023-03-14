package com.example.sudokusolver.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.sudokusolver.utils.Constants.BIG_STROKE_WIDTH
import com.example.sudokusolver.utils.Constants.BOARD_SIZE
import com.example.sudokusolver.utils.Constants.BOX_SIZE
import com.example.sudokusolver.utils.Constants.MEDIUM_STROKE_WIDTH
import com.example.sudokusolver.utils.Constants.SMALL_STROKE_WIDTH
import com.example.sudokusolver.R
import com.example.sudokusolver.logic.SolverImpl

class BoardView(context: Context, attrs: AttributeSet) : View(context, attrs), Board {

    val solver by lazy { SolverImpl() }
    private val attributes = context.obtainStyledAttributes(attrs, R.styleable.BoardView)

    private val boardPaint = Paint()
    private var selectedCellPaint = Paint()
    private var highlightedCellPaint = Paint()
    private val textPaint: Paint = Paint()
    private var textRect = Rect()

    private var boardColor: Int? = null
    private var boxColor: Int? = null
    private var cellColor: Int? = null
    private var textColor: Int? = null
    private var solvedTextColor: Int? = null
    private var selectedTextColor: Int? = null
    private var selectedCellColor: Int? = null
    private var highlightedCellColor: Int? = null
    private var cellSize: Int? = null


    init {
        extractAttributes()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = if (measuredWidth < measuredHeight) measuredWidth else measuredHeight
        cellSize = size / BOARD_SIZE
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val (selectedRow, selectedColumn) = solver.getSelection()
        drawSelectedCell(canvas, selectedRow, selectedColumn)
        drawHighlightedRowAndColumn(canvas, selectedRow, selectedColumn)
        drawHighlightBox(canvas, selectedRow, selectedColumn)
        drawGrid(canvas)
        drawBoard(canvas)
        drawNumbers(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        if (event.action == MotionEvent.ACTION_DOWN) {
            val row = event.y.toInt() / cellSize!!
            val column = event.x.toInt() / cellSize!!
            if (row < BOARD_SIZE && column < BOARD_SIZE) solver.setSelection(row, column)
            invalidate()
        }
        return true
    }

    override fun extractAttributes() {
        try {
            boardColor = attributes.getColor(R.styleable.BoardView_boardColor, 0)
            boxColor = attributes.getColor(R.styleable.BoardView_boxColor, 0)
            cellColor = attributes.getColor(R.styleable.BoardView_cellColor, 0)
            textColor = attributes.getColor(R.styleable.BoardView_textColor, 0)
            solvedTextColor = attributes.getColor(R.styleable.BoardView_solvedTextColor, 0)
            selectedTextColor = attributes.getColor(R.styleable.BoardView_selectedTextColor, 0)
            selectedCellColor = attributes.getColor(R.styleable.BoardView_selectedCellColor, 0)
            highlightedCellColor =
                attributes.getColor(R.styleable.BoardView_highlightedCellColor, 0)
        } finally {
            attributes.recycle()
        }
    }

    override fun drawBoard(canvas: Canvas) {
        setBoardProperties()
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), boardPaint)
    }

    override fun drawGrid(canvas: Canvas) {
        for (i in 0 until BOARD_SIZE) {
            if (i % BOX_SIZE == 0) {
                setBoxProperties()
            } else {
                setCellProperties()
            }
            drawHorizontalLines(canvas, i)
            drawVerticalLines(canvas, i)
        }
    }

    override fun drawVerticalLines(canvas: Canvas, index: Int) {
        canvas.drawLine(
            (index * cellSize!!).toFloat(),
            0f,
            (index * cellSize!!).toFloat(),
            height.toFloat(),
            boardPaint
        )
    }

    override fun drawHorizontalLines(canvas: Canvas, index: Int) {
        canvas.drawLine(
            0f,
            (index * cellSize!!).toFloat(),
            width.toFloat(),
            (index * cellSize!!).toFloat(),
            boardPaint
        )
    }

    override fun drawNumbers(canvas: Canvas) {
        textPaint.textSize = cellSize!!.toFloat()
        textPaint.textAlign = Paint.Align.CENTER
        for (row in 0 until BOARD_SIZE) {
            for (column in 0 until BOARD_SIZE) {
                if (solver.isNotEmpty(row, column)) {
                    drawNumber(canvas, row, column)
                }
            }
        }
    }

    override fun drawNumber(canvas: Canvas, row: Int, column: Int) {
        val number = solver.getSelectedValue(row, column).toString()
        textPaint.getTextBounds(number, 0, number.length, textRect)
        val x = (column * cellSize!! + cellSize!! / 2).toFloat()
        val y = (row * cellSize!! + cellSize!! / 2).toFloat()
        textPaint.getTextBounds(number, 0, 1, textRect)
        val textHeight = textRect.height()
        val textOffset = (textHeight / 2).toFloat()
        setTextProperties(row, column)
        canvas.drawText(number, x, y + textOffset, textPaint)
    }

    override fun drawSelectedCell(canvas: Canvas, selectedRow: Int, selectedColumn: Int) {
        if (solver.isSelected(selectedRow, selectedColumn)) {
            setSelectedCellProperties()
            canvas.drawRect(
                selectedColumn * cellSize!!.toFloat(),
                selectedRow * cellSize!!.toFloat(),
                (selectedColumn + 1) * cellSize!!.toFloat(),
                (selectedRow + 1) * cellSize!!.toFloat(),
                selectedCellPaint
            )
        }
    }

    override fun drawHighlightedRowAndColumn(
        canvas: Canvas,
        selectedRow: Int,
        selectedColumn: Int
    ) {
        setHighlightCellsProperties()
        for (i in 0 until BOARD_SIZE) {
            if (i != selectedRow) {
                drawHighlightedCell(canvas, i, selectedColumn)
            }
            if (i != selectedColumn) {
                drawHighlightedCell(canvas, selectedRow, i)
            }
        }
    }

    override fun drawHighlightedCell(canvas: Canvas, line: Int, index: Int) {
        canvas.drawRect(
            (index * cellSize!!).toFloat(),
            (line * cellSize!!).toFloat(),
            (index * cellSize!! + cellSize!!).toFloat(),
            (line * cellSize!! + cellSize!!).toFloat(),
            highlightedCellPaint
        )
    }

    override fun drawHighlightBox(canvas: Canvas, selectedRow: Int, selectedColumn: Int) {
        val boxRow = selectedRow / BOX_SIZE
        val boxColumn = selectedColumn / BOX_SIZE
        for (row in 0 until BOX_SIZE) {
            for (column in 0 until BOX_SIZE) {
                if (solver.isSelectedBox(row, column)) {
                    drawHighlightedCell(
                        canvas,
                        boxRow * BOX_SIZE + row,
                        boxColumn * BOX_SIZE + column
                    )
                }
            }
        }
    }

    override fun setBoardProperties() {
        boardColor?.let { boardPaint.color = it }
        with(boardPaint) {
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = BIG_STROKE_WIDTH
        }
    }

    override fun setBoxProperties() {
        boxColor?.let { boardPaint.color = it }
        with(boardPaint) {
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = MEDIUM_STROKE_WIDTH
        }
    }

    override fun setCellProperties() {
        cellColor?.let { boardPaint.color = it }
        with(boardPaint) {
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = SMALL_STROKE_WIDTH
        }
    }

    override fun setSelectedCellProperties() {
        selectedCellColor?.let { selectedCellPaint.color = it }
        with(selectedCellPaint) {
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    override fun setHighlightCellsProperties() {
        highlightedCellColor?.let { highlightedCellPaint.color = it }
        with(highlightedCellPaint) {
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    override fun setTextProperties(row: Int, column: Int) {
        with(textPaint) {
            style = Paint.Style.FILL
            isAntiAlias = true
            textSize = cellSize!!.toFloat()
        }
        when {
            solver.isSelected(row, column) -> {
                selectedTextColor?.let { textPaint.color = it }
            }

            solver.isUserInserted(row, column) -> {
                textColor?.let { textPaint.color = it }
            }

            else -> {
                solvedTextColor?.let { textPaint.color = it }
            }

        }
    }

}