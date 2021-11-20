package com.kodama.rdoku

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.ceil

class SudokuBoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var boardColor = 0
    private var cellFillColor = 0
    private var highlightColor = 0
    private var cellSize = 0

    private val boardColorPaint: Paint = Paint()
    private val cellFillColorPaint: Paint = Paint()
    private val highlightColorPaint: Paint = Paint()
    private val numberPaint: Paint = Paint()

    private var numberColor = 0
    private var collisionNumberColor: Int = 0

    private val numberPaintBounds: Rect = Rect()

    private val sudokuGame = SudokuGame()

    init {
        if(context != null && attrs != null){
            val themeArray: TypedArray = context.theme
                .obtainStyledAttributes(attrs, R.styleable.SudokuBoardView,
                    0, 0)

            boardColor = themeArray.getInt(R.styleable.SudokuBoardView_boardColor, 0)
            cellFillColor = themeArray.getInt(R.styleable.SudokuBoardView_cellFillColor, 0)
            highlightColor = themeArray.getInt(R.styleable.SudokuBoardView_highlightColor, 0)
            numberColor = themeArray.getInt(R.styleable.SudokuBoardView_numberColor, 0)
            collisionNumberColor = themeArray.getInt(R.styleable.SudokuBoardView_collisionNumberColor, 0)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val dimension: Int = Math.min(width, height)
        cellSize = dimension / 9
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

        colorCell(canvas, sudokuGame.getSelectedRow(), sudokuGame.getSelectedCol())
        canvas?.drawRect(0f,0f, measuredWidth.toFloat(), measuredHeight.toFloat(), boardColorPaint)
        drawBoard(canvas)
        drawText(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event == null){
            return false
        }
        var isValid: Boolean

        if(event.action == MotionEvent.ACTION_DOWN){
            sudokuGame.setSelectedRow(ceil(event.y / cellSize).toInt())
            sudokuGame.setSelectedCol(ceil(event.x / cellSize).toInt())

            isValid = true
        }
        else {
            isValid = false
        }

        return isValid
    }

    private fun drawText(canvas: Canvas?){

        for(row: Int in 0 until 9){
            for(col: Int in 0 until 9){
                if(sudokuGame.getBoard()[row][col].value != 0){
                    val text: String = sudokuGame.getBoard()[row][col].value.toString()

                    numberPaint.getTextBounds(text, 0, text.length, numberPaintBounds)
                    val width: Float = numberPaint.measureText(text)
                    val height: Float = numberPaintBounds.height().toFloat()


                    canvas?.drawText(text, (col * cellSize) + ((cellSize - width) / 2f),
                        (row * cellSize + cellSize) - ((cellSize - height) / 2f), numberPaint)
                }
            }
        }
    }
    private fun colorCell(canvas: Canvas?, row: Int, col: Int){
        if(canvas != null
            && sudokuGame.getSelectedRow() != -1 && sudokuGame.getSelectedCol() != -1){
            // Paint column
            canvas.drawRect((col - 1f) * cellSize, 0f,
                (col * cellSize).toFloat(), (cellSize * 9).toFloat(), highlightColorPaint)

            // Paint row
            canvas.drawRect(0f, (row - 1f) * cellSize,
                (cellSize * 9).toFloat(), (row * cellSize).toFloat(), highlightColorPaint)

            // Paint tapped cell
            canvas.drawRect((col - 1f) * cellSize, (row - 1f) * cellSize,
                (col * cellSize).toFloat(), (row * cellSize).toFloat(), highlightColorPaint)
        }

        // Update view
        invalidate()
    }

    private fun drawBoard(canvas: Canvas?){
        // Draw vertical lines
        for(col in 0..9){
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
        for(row in 0..9){
            if(row % 3 == 0){
                setThickLine()
            }
            else{
                setThinLine()
            }

            canvas?.drawLine(0f, (cellSize * row).toFloat(),
                this.width.toFloat(), (cellSize * row).toFloat() , boardColorPaint)
        }
    }

    private fun setThickLine(){
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 12f
        boardColorPaint.color = boardColor
    }
    private fun setThinLine(){
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 4f
        boardColorPaint.color = boardColor
    }
}
