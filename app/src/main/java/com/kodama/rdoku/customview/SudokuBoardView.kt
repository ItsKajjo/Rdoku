package com.kodama.rdoku.customview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.preference.PreferenceManager
import com.kodama.rdoku.R
import com.kodama.rdoku.model.BoardState
import com.kodama.rdoku.model.SudokuCell
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.sqrt

class SudokuBoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var boardColor = 0
    private var cellFillColor = 0
    private var highlightColor = 0
    private var cellSize = 0
    private var lockedNumColor = 0
    private var numberColor = 0
    private var mistakeColor: Int = 0

    private val boardColorPaint: Paint = Paint()
    private val cellFillColorPaint: Paint = Paint()
    private val highlightColorPaint: Paint = Paint()
    private val numberPaint: Paint = Paint()

    private val numberPaintBounds: Rect = Rect()

    private var isIdenticalNumHighlighted = true
    private var isMistakesHighlighted = true

    var gridSize: Int = 9
    var selectedRow = -1
    var selectedCol = -1

    private lateinit var board: Array<Array<SudokuCell>>
    var boardState = BoardState.Incomplete

    init {
        if(context != null && attrs != null){
            val themeArray: TypedArray = context.theme
                .obtainStyledAttributes(attrs, R.styleable.SudokuBoardView,
                    0, 0)

            boardColor = themeArray.getInt(R.styleable.SudokuBoardView_boardColor, 0)
            cellFillColor = themeArray.getInt(R.styleable.SudokuBoardView_cellFillColor, 0)
            highlightColor = themeArray.getInt(R.styleable.SudokuBoardView_highlightColor, 0)
            numberColor = themeArray.getInt(R.styleable.SudokuBoardView_numberColor, 0)
            lockedNumColor = themeArray.getInt(R.styleable.SudokuBoardView_lockedNumColor, 0)
            mistakeColor = themeArray.getInt(R.styleable.SudokuBoardView_mistakeColor, 0)

            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            isIdenticalNumHighlighted = prefs.getBoolean("highlight_identical_numbers", true)
            isMistakesHighlighted = prefs.getBoolean("highlight_mistakes", true)

            themeArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val dimension: Int = min(width, height)

        cellSize = dimension / gridSize
        setMeasuredDimension(dimension, dimension)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 16f
        boardColorPaint.color = boardColor
        boardColorPaint.isAntiAlias = true

        cellFillColorPaint.style = Paint.Style.FILL
        cellFillColorPaint.color = cellFillColor
        cellFillColorPaint.isAntiAlias = true

        highlightColorPaint.style = Paint.Style.FILL
        highlightColorPaint.color = highlightColor
        highlightColorPaint.isAntiAlias = true

        numberPaint.style = Paint.Style.FILL
        numberPaint.color = numberColor
        numberPaint.isAntiAlias = true
        numberPaint.textSize = cellSize.toFloat()

        canvas.apply {
            colorCells(canvas, selectedRow + 1, selectedCol + 1)

            if(isIdenticalNumHighlighted){
                highlightIdenticalNumbers(canvas)
            }

            canvas?.drawRect(0f,0f, measuredWidth.toFloat(), measuredHeight.toFloat(), boardColorPaint)
            drawBoard(canvas)
            drawText(canvas)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event == null || boardState == BoardState.Complete){
            return false
        }

        if(event.action == MotionEvent.ACTION_DOWN){
            selectedRow = floor(event.y / cellSize).toInt()
            selectedCol = floor(event.x / cellSize).toInt()

            invalidate()
            callOnClick()

            return true
        }

        return false
    }

    private fun drawText(canvas: Canvas?){
        for(row: Int in 0 until gridSize){
            for(col: Int in 0 until gridSize){
                if(board[row][col].value != 0){
                    val text: String = board[row][col].value.toString()

                    numberPaint.getTextBounds(text, 0, text.length, numberPaintBounds)
                    val width: Float = numberPaint.measureText(text)
                    val height: Float = numberPaintBounds.height().toFloat()

                    if(board[row][col].locked){
                        numberPaint.color = lockedNumColor
                    }

                    if(board[row][col].wrong && isMistakesHighlighted){
                        numberPaint.color = mistakeColor
                    }

                    canvas?.drawText(text, (col * cellSize) + ((cellSize - width) / 2f),
                        (row * cellSize + cellSize) - ((cellSize - height) / 2f), numberPaint)

                    numberPaint.color = numberColor
                }
            }
        }
    }

    private fun colorCells(canvas: Canvas?, row: Int, col: Int){
        if(canvas != null && selectedRow != -1 && selectedCol != -1){
            // Paint column
            canvas.drawRect((col - 1f) * cellSize, 0f,
                (col * cellSize).toFloat(), (cellSize * gridSize).toFloat(), highlightColorPaint)

            // Paint row
            canvas.drawRect(0f, (row - 1f) * cellSize,
                (cellSize * gridSize).toFloat(), (row * cellSize).toFloat(), highlightColorPaint)

            // Paint tapped cell
            canvas.drawRect((col - 1f) * cellSize, (row - 1f) * cellSize,
                (col * cellSize).toFloat(), (row * cellSize).toFloat(), highlightColorPaint)
        }
    }

    private fun drawBoard(canvas: Canvas?){
        // Draw vertical lines
        for(col in 0..gridSize){
            if(col % 3 == 0){
                setThickLine()
            }
            else{
                setThinLine()
            }
            canvas?.drawLine((cellSize * col).toFloat(), 0f,
                (cellSize * col).toFloat(), this.width.toFloat(), boardColorPaint)
        }

        // Draw horizontal lines
        for(row in 0..gridSize){
            if(row % sqrt(gridSize.toDouble()).toInt() == 0){
                setThickLine()
            }
            else{
                setThinLine()
            }

            canvas?.drawLine(0f, (cellSize * row).toFloat(),
                this.width.toFloat(), (cellSize * row).toFloat() , boardColorPaint)
        }
    }

    private fun highlightIdenticalNumbers(canvas: Canvas?){
        val row = selectedRow
        val col = selectedCol
        if(row > 0 && col > 0){
            for(i in 0 until gridSize){
                for(j in 0 until gridSize){
                    if(board[i][j].value == 0){
                        continue
                    }

                    // finding identical numbers and highlight them
                    if(board[i][j].value == board[row][col].value){
                        canvas?.drawRect((j * cellSize).toFloat(), (i * cellSize).toFloat(),
                            (j * cellSize + cellSize).toFloat(), (i * cellSize + cellSize).toFloat(), cellFillColorPaint)
                    }
                }
            }
        }
    }

    fun setBoard(board: Array<Array<SudokuCell>>){
        this.board = board
        gridSize = board.size
        invalidate()
    }

    fun getBoard() : Array<Array<SudokuCell>> = board

    fun setNum(num: Int, row: Int = selectedRow, col: Int = selectedCol){
        if(row != -1 && col != -1) {
            if(board[row][col].value == num){
                board[row][col].value = 0
            }
            else{
                board[row][col].value = num
            }
            invalidate()
        }
    }
    fun setNumberWrong(wrong: Boolean, row: Int = selectedRow, col: Int = selectedCol){
        if(row != -1 && col != -1){
            board[row][col].wrong = wrong
            invalidate()
        }
    }
    fun resetBoard(){
        if(this::board.isInitialized){
            for(row in 0 until gridSize){
                for(col in 0 until gridSize){
                    board[row][col].value = 0
                    board[row][col].wrong = false
                    board[row][col].locked= false

                }
            }
        }
    }
    private fun setThickLine(){
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 14f
        boardColorPaint.color = boardColor
    }

    private fun setThinLine(){
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 6f
        boardColorPaint.color = boardColor
    }
}
