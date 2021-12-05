package com.kodama.rdoku.gamelogic

import android.content.Context
import android.util.Log
import com.kodama.rdoku.R
import com.kodama.rdoku.model.SudokuCell
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class SudokuGame(private var context: Context?){

    companion object{
        var mainBoard: Array<Array<SudokuCell>> = arrayOf(
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
        )

        var selectedRow: Int = -1
        var selectedCol: Int = -1

        var locked = false
    }

    private var solvedBoard: Array<Array<SudokuCell>> = arrayOf(
        Array(9){ SudokuCell(1) },
        Array(9){ SudokuCell(1) },
        Array(9){ SudokuCell(1) },
        Array(9){ SudokuCell(1) },
        Array(9){ SudokuCell(1) },
        Array(9){ SudokuCell(1) },
        Array(9){ SudokuCell(1) },
        Array(9){ SudokuCell(1) },
        Array(9){ SudokuCell(1) },
    )

    fun setNumberBoard(number: Int){
        if(selectedRow != -1 && selectedCol != -1 && !mainBoard[selectedRow - 1][selectedCol - 1].locked){
            // Erase number on double click
            if(mainBoard[selectedRow - 1][selectedCol - 1].value == number){
                mainBoard[selectedRow - 1][selectedCol - 1].value = 0
            }

            else{
                mainBoard[selectedRow - 1][selectedCol - 1].value = number
            }

            mainBoard[selectedRow - 1][selectedCol - 1].wrong = false

            if(!isValidNumber(mainBoard[selectedRow - 1][selectedCol - 1].value)){
                mainBoard[selectedRow - 1][selectedCol - 1].wrong = true
            }
        }
    }

    fun useHint(){
        if(selectedRow != -1 && selectedCol != -1){
            setNumberBoard(solvedBoard[selectedRow - 1][selectedCol - 1].value)
            mainBoard[selectedRow - 1][selectedCol - 1].wrong = false
            Log.i("HINT", "Hint was used at row:$selectedRow;Col$selectedCol")
        }
    }

    fun eraseNumber(){
        setNumberBoard(0)
    }

    fun debugSolve(){
        for(i in 0 until 9){
            for(j in 0 until 9){
                mainBoard[i][j].value = solvedBoard[i][j].value
            }
        }
    }


    fun checkForComplete():Boolean{
        for(i in 0 until 9){
            for(j in 0 until 9){
                if(mainBoard[i][j].value == 0){
                    return false
                }
            }
        }

        for(i in 0 until 9){
            for(j in 0 until 9){
                if(mainBoard[i][j].value != solvedBoard[i][j].value){
                    return false
                }
            }
        }
        return true
    }

    private fun solveBoard(){
        // lmao I know how terrible it looks
        // but I don't know how to do it better..
        // companion objects are the worst thing I've seen
        // some shit with memory addresses

        val mutableBoard: MutableList<Array<SudokuCell>> = mutableListOf()

        for(i in 0 until 9){
            val tempRow = arrayOf(
                SudokuCell(0), SudokuCell(0), SudokuCell(0),
                SudokuCell(0), SudokuCell(0), SudokuCell(0),
                SudokuCell(0), SudokuCell(0), SudokuCell(0), )

            for(j in 0 until 9){
                tempRow[j].value = mainBoard[i][j].value
            }
            mutableBoard.add(tempRow)
        }
        val tempBoard: Array<Array<SudokuCell>> = arrayOf(
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },)

        for(i in 0 until 9){
            for(j in 0 until 9){
                tempBoard[i][j].value = mutableBoard[i][j].value
            }
        }

        val sudokuGenSolve = SudokuGenSolve()
        sudokuGenSolve.gameBoard = tempBoard

        val temp = sudokuGenSolve.solve()

        if(temp.first){
            solvedBoard = temp.second
        }

        resetWrongNumbers()
    }

    fun isValidNumber(number: Int):Boolean{
        for(i: Int in 0 until 9){
            // Check column
            if(i != selectedCol - 1 && mainBoard[selectedRow - 1][i].value == number){
                return false
            }
            // Check row
            if(i != selectedRow - 1 && mainBoard[i][selectedCol - 1].value == number){
                return false
            }
        }

        // Just for better readability
        val row = selectedRow - 1
        val col = selectedCol - 1

        // Check block
        for(i: Int in (row - (row % 3)) until (row - (row % 3) + 3)){
            for(j: Int in (col - (col % 3)) until (col - (col % 3) + 3)){
                if(i != row && j != col && mainBoard[i][j].value == number){
                    return false
                }
            }
        }
        return true
    }


    fun generateBoard(spots: Int){
        resetBoard()
        var tries = 0
        val sudokuGenSolve = SudokuGenSolve()

        val msElapsed: Long = measureTimeMillis {
            val generatedBoard = sudokuGenSolve.generate(spots)
            if(generatedBoard.first){
                mainBoard = generatedBoard.second
                tries = generatedBoard.third
            }
        }

        Log.d("GenerateBoard", "Board Generated! Time elapsed: $msElapsed ms, $tries tries")
        solveBoard()

        locked = false
        lockNumbers()
    }

    // Sets board from resources (hardBoards.xml). Because it takes a long time to generate boards like these
    fun setHardBoard(){
        if(context != null){
            resetBoard()
            val hardBoards: Array<String> = context!!.resources.getStringArray(R.array.hardBoards)

            val selectedBoard: String = hardBoards[Random.nextInt(0, hardBoards.lastIndex)]
            var stringIndex = 0

            for(i in 0 until 9){
                for(j in 0 until 9){
                    mainBoard[i][j].value = selectedBoard[stringIndex].digitToInt()
                    stringIndex += 1
                }
            }

            solveBoard()

            locked = false
            lockNumbers()
        }
    }

    private fun lockNumbers(){
        if(!locked){
            for(i in 0 until 9){
                for(j in 0 until 9){
                    mainBoard[i][j].locked = mainBoard[i][j].value != 0
                }
            }
            locked = true
        }
    }

    private fun resetWrongNumbers(){
        for(i in 0 until 9){
            for(j in 0 until 9){
                mainBoard[i][j].wrong = false
            }
        }
    }

    fun numberUsedNineTimes(number: Int):Boolean{
        if(number == 0){
            return false
        }

        var count = 0

        for(i in 0 until 9){
            for(j in 0 until 9){
                if(mainBoard[i][j].value == number){
                    count += 1
                }
            }
        }

        if(count != 9){
            return false
        }

        return true
    }

    private fun resetBoard(){
        mainBoard = arrayOf(
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
            Array(9){ SudokuCell(0) },
        )
    }
}