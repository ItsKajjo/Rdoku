package com.kodama.rdoku

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class SudokuBoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var boardColor: Int = 0
    private val boardColorPaint: Paint
    private var cellSize: Int = 0


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val dimension: Int = Math.min(width, height)
        cellSize = dimension / 9
        setMeasuredDimension(dimension, dimension)

    }

    init {
        if(context != null && attrs != null){
            val themeArray: TypedArray = context.theme
                .obtainStyledAttributes(attrs, R.styleable.SudokuBoardView,
                    0, 0)

            boardColor = themeArray.getInt(R.styleable.SudokuBoardView_boardColor, 0)
        }
        boardColorPaint = Paint()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 16f
        boardColorPaint.color = boardColor
        boardColorPaint.isAntiAlias = true

        canvas?.drawRect(0f,0f, measuredWidth.toFloat(), measuredHeight.toFloat(), boardColorPaint)
        drawBoard(canvas)
    }

    private fun drawBoard(canvas: Canvas?){
        for(col in 0..9){
            if(col % 3 == 0){
                drawThickLine()
            }
            else{
                drawThinLine()
            }
            canvas?.drawLine((cellSize * col).toFloat(), 0f,
                (cellSize * col).toFloat(), this.width.toFloat(), boardColorPaint)
        }

        for(row in 0..9){
            if(row % 3 == 0){
                drawThickLine()
            }
            else{
                drawThinLine()
            }

            canvas?.drawLine(0f, (cellSize * row).toFloat(),
                this.width.toFloat(), (cellSize * row).toFloat() , boardColorPaint)
        }
    }

    private fun drawThickLine(){
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 8f
        boardColorPaint.color = boardColor
    }
    private fun drawThinLine(){
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 4f
        boardColorPaint.color = boardColor
    }
}
