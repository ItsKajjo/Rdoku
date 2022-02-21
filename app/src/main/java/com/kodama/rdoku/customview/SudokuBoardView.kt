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
import com.kodama.rdoku.gamelogic.SudokuGame
import kotlin.math.ceil
import kotlin.math.sqrt

class SudokuBoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var boardColor = 0
    private var cellFillColor = 0
    private var highlightColor = 0
    private var cellSize = 0
    private var lockedNumColor = 0

    private val boardColorPaint: Paint = Paint()
    private val cellFillColorPaint: Paint = Paint()
    private val highlightColorPaint: Paint = Paint()
    private val numberPaint: Paint = Paint()

    private var numberColor = 0
    private var collisionNumberColor: Int = 0
    private val numberPaintBounds: Rect = Rect()

    private var isIdenticalNumHighlighted = true

    var gridSize: Int = 9

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
            collisionNumberColor = themeArray.getInt(R.styleable.SudokuBoardView_collisionNumberColor, 0)

            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            isIdenticalNumHighlighted = prefs.getBoolean("highlight_identical_numbers", true)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val dimension: Int = Math.min(width, height)

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

        colorCells(canvas, SudokuGame.selectedRow, SudokuGame.selectedCol)

        if(isIdenticalNumHighlighted){
            highlightIdenticalNumbers(canvas)
        }

        canvas?.drawRect(0f,0f, measuredWidth.toFloat(), measuredHeight.toFloat(), boardColorPaint)
        drawBoard(canvas)
        drawText(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event == null){
            return false
        }

        if(event.action == MotionEvent.ACTION_DOWN){
            SudokuGame.selectedRow = ceil(event.y / cellSize).toInt()
            SudokuGame.selectedCol = ceil(event.x / cellSize).toInt()

            invalidate()
            return true
        }

        return false
    }

    private fun drawText(canvas: Canvas?){

        for(row: Int in 0 until gridSize){
            for(col: Int in 0 until gridSize){
                if(SudokuGame.mainBoard[row][col].value != 0){
                    val text: String = SudokuGame.mainBoard[row][col].value.toString()

                    numberPaint.getTextBounds(text, 0, text.length, numberPaintBounds)
                    val width: Float = numberPaint.measureText(text)
                    val height: Float = numberPaintBounds.height().toFloat()

                    if(SudokuGame.mainBoard[row][col].locked){
                        numberPaint.color = lockedNumColor
                    }

                    if(SudokuGame.mainBoard[row][col].wrong){
                        numberPaint.color = collisionNumberColor
                    }

                    canvas?.drawText(text, (col * cellSize) + ((cellSize - width) / 2f),
                        (row * cellSize + cellSize) - ((cellSize - height) / 2f), numberPaint)

                    numberPaint.color = numberColor
                }
            }
        }
    }

    private fun colorCells(canvas: Canvas?, row: Int, col: Int){
        if(canvas != null
            && SudokuGame.selectedRow != -1 && SudokuGame.selectedCol != -1){
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
        val row = SudokuGame.selectedRow
        val col = SudokuGame.selectedCol
        if(row > 0 && col > 0){
            for(i in 0 until gridSize){
                for(j in 0 until gridSize){
                    if(SudokuGame.mainBoard[i][j].value == 0){
                        continue
                    }

                    // finding identical numbers and highlight them
                    if(SudokuGame.mainBoard[i][j].value == SudokuGame.mainBoard[row - 1][col - 1].value){
                        canvas?.drawRect((j * cellSize).toFloat(), (i * cellSize).toFloat(),
                            (j * cellSize + cellSize).toFloat(), (i * cellSize + cellSize).toFloat(), cellFillColorPaint)
                    }
                }
            }
        }
    }

    private fun setThickLine(){
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = (gridSize + (gridSize / 2)).toFloat()
        boardColorPaint.color = boardColor
    }

    private fun setThinLine(){
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = ((gridSize + (gridSize / 2) + 2) / 2).toFloat()
        boardColorPaint.color = boardColor
    }
}
