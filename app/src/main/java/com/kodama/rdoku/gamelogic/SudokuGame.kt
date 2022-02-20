package com.kodama.rdoku.gamelogic

import android.content.Context
import android.util.Log
import com.kodama.rdoku.R
import com.kodama.rdoku.model.BoardState
import com.kodama.rdoku.model.GameDifficulty
import com.kodama.rdoku.model.GameType
import com.kodama.rdoku.model.SudokuCell
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class SudokuGame(private var context: Context?, val size: Int){
    var boardState = BoardState.Incomplete

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

    private var solvedBoard: Array<Array<SudokuCell>> = Array(size){Array(9){ SudokuCell(1) }}

    fun setNumberBoard(number: Int){
        if(selectedRow != -1 && selectedCol != -1 && !mainBoard[selectedRow - 1][selectedCol - 1].locked){
            // Erase number on double set
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
        for(i in 0 until size){
            for(j in 0 until size){
                mainBoard[i][j].value = solvedBoard[i][j].value
            }
        }
    }


    fun checkForComplete():Boolean{
        for(i in 0 until size){
            for(j in 0 until size){
                if(mainBoard[i][j].value == 0){
                    return false
                }
            }
        }

        for(i in 0 until size){
            for(j in 0 until size){
                if(mainBoard[i][j].value != solvedBoard[i][j].value){
                    return false
                }
            }
        }

        boardState = BoardState.Complete
        return true
    }

    private fun solveBoard(){
        val solver = Solver()
        solvedBoard = solver.solve(mainBoard, size)

        resetWrongNumbers()
    }

    fun isValidNumber(number: Int):Boolean{
        val solver = Solver()
        return solver.validMove(selectedRow - 1, selectedCol - 1, number,  mainBoard, size)
    }


    // sets board from resources
    fun setBoard(difficulty: GameDifficulty, gameType: GameType){
        if(context != null){
            resetBoard()

            var boards: Array<String> = context!!.resources.getStringArray(R.array.easyBoards9x9)

            if(gameType == GameType.classic_9x9){
                boards = when(difficulty){
                    GameDifficulty.Easy ->{
                        context!!.resources.getStringArray(R.array.easyBoards9x9)
                    }

                    GameDifficulty.Moderate ->{
                        context!!.resources.getStringArray(R.array.moderateBoards9x9)
                    }

                    GameDifficulty.Hard ->{
                        context!!.resources.getStringArray(R.array.hardBoards9x9)
                    }
                }
            }

            else if(gameType == GameType.classic_6x6){
                boards = context!!.resources.getStringArray(R.array.easyBoards6x6)
            }

            val selectedBoard: String = boards[Random.nextInt(0, boards.size)]

            for(i in 0 until size){
                for(j in 0 until size){
                    mainBoard[i][j].value = selectedBoard[i * size + j].digitToInt()
                }
            }

            solveBoard()

            locked = false
            lockNumbers()
        }
    }

    private fun lockNumbers(){
        if(!locked){
            for(i in 0 until size){
                for(j in 0 until size){
                    mainBoard[i][j].locked = mainBoard[i][j].value != 0
                }
            }
            locked = true
        }
    }

    private fun resetWrongNumbers(){
        for(i in 0 until size){
            for(j in 0 until size){
                mainBoard[i][j].wrong = false
            }
        }
    }

    fun fullyUsedNumber(number: Int):Boolean{
        if(number == 0){
            return false
        }

        var count = 0

        for(i in 0 until size){
            for(j in 0 until size){
                if(mainBoard[i][j].value == number){
                    count += 1
                }
            }
        }

        if(count != size){
            return false
        }

        return true
    }

    private fun resetBoard(){
        mainBoard = Array(9) {Array(9){ SudokuCell(0) }}
    }
}