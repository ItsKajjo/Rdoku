package com.kodama.rdoku.gamelogic

import android.util.Log
import com.kodama.rdoku.model.SudokuCell

class Solver {
    var size = 6

    // solves and returns the given board
    fun solve(board: Array<Array<SudokuCell>>, size: Int): Array<Array<SudokuCell>>{
        var grid:Array<Array<Int>> = Array(size) { Array(size) { 0 } }

        for(i in 0 until size){
            for(j in 0 until size){
                grid[i][j] = board[i][j].value
            }
        }

        if(size == 6){
            grid = solver6x6(0,0,grid).second
        }
        else if(size == 9){
            grid = solve9x9(grid).second
        }

        val solvedBoard: Array<Array<SudokuCell>> = Array(9){ Array(9) { SudokuCell(0) } }
        for(i in 0 until size){
            for(j in 0 until size){
                solvedBoard[i][j].value = grid[i][j]
                solvedBoard[i][j].locked = board[i][j].locked
            }
        }

        return solvedBoard
    }

    // returns if it possible to set num to given row;col at the given grid
    fun validMove(row: Int, col: Int, num:Int, grid: Array<Array<SudokuCell>>, size: Int):Boolean{
        val intGrid: Array<Array<Int>> = Array(size) { Array(size) { 0 } }

        for(i in 0 until size){
            for(j in 0 until size){
                intGrid[i][j] = grid[i][j].value
            }
        }

        return when(size){
            9 -> validMove9x9(row, col, num, intGrid)
            6 -> validMove6x6(row, col, num, intGrid)
            else -> false
        }
    }

    // can't find/make a universal algorithm for valid move/solving sudoku of different sizes, so there are two of each for and 9x9 and 6x6

    private fun solver6x6(r: Int, c: Int, puz: Array<Array<Int>>): Pair<Boolean, Array<Array<Int>>>{
        var row = r
        var col = c
        if (row == this.size) {
            row = 0
            if (col++ == this.size - 1) {
                return Pair(true, puz)
            }
        }
        if (puz[row][col] != 0) {
            return solver6x6(row + 1, col, puz)
        }
        for (i in 1..this.size) {
            if (validMove6x6(row, col, i, puz)) {
                puz[row][col] = i
                if (solver6x6(row + 1, col, puz).first) {
                    return Pair(true, puz)
                }
            }
        }
        puz[row][col] = 0
        return Pair(false, puz)
    }

    private fun solve9x9(grid: Array<Array<Int>>) : Pair<Boolean, Array<Array<Int>>>{
        var row = -1
        var col = -1
        var isEmpty = true

        for(i in 0 until 9){
            for(j in 0 until 9){
                if(grid[i][j] == 0){
                    row = i
                    col = j

                    isEmpty = false
                    break
                }
            }
            if(!isEmpty){
                break
            }
        }

        if(isEmpty){

            return Pair(true, grid)
        }

        for(i in 1..9){
            if(validMove9x9(row, col, i, grid)){
                grid[row][col] = i

                if(solve9x9(grid).first){
                    return Pair(true, grid)
                }else{
                    grid[row][col] = 0
                }
            }
        }
        return Pair(false, grid)
    }

    private fun validMove9x9(row: Int, col: Int, num: Int, grid: Array<Array<Int>>):Boolean{
        for(i in 0 until grid.size){
            if((grid[row][i] == num && i != col) || (grid[i][col] == num && i != row)){
                return false
            }
        }

        for(i in row - row % 3 until (row - row % 3) + 3){
            for(j in col - col % 3 until (col - col % 3) + 3){
                if(grid[i][j] == num && i != row && i != col){
                    return false
                }
            }
        }

        return true
    }

    private fun validMove6x6(r: Int, c: Int, num: Int, puz: Array<Array<Int>>): Boolean {
        // check horizontal and vertical
        for (i in 0 until size) {
            if ((puz[r][i] == num && puz[r][i] != 0 && c != i) || (puz[i][c] == num && puz[i][c] != 0 && r != i)){
                return false
            }
        }

        val boxResult = getBoxColAndRow6x6(r,c)
        val boxRow = boxResult.first
        val boxCol = boxResult.second

        // check in box
        for (i in boxRow until boxRow + 2) {
            for (j in boxCol until boxCol + 3) {
                if (puz[i][j] == num && i != r && j != c){
                    return false
                }
            }
        }
        return true
    }

    // returns starting coordinates in box 2x3 (in 6x6 sudoku there are 5 of them)
    // I've tried to do it mathematically, like in 9x9 sudoku, but due to the size box is not NxN I didn't find the solution
    private fun getBoxColAndRow6x6(r: Int, c: Int):Pair<Int, Int>{
        var boxRow = -1
        var boxCol = -1

        if (c < 3) boxCol = 0
        else if (c in 3..5) boxCol = 3

        boxRow = if(r >= 4) 4
        else if(r in 2..3) 2
        else 0

        return Pair(boxRow, boxCol)
    }
}