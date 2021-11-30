package com.kodama.rdoku

import com.kodama.rdoku.gamelogic.SudokuGame
import org.junit.Assert
import org.junit.Test

class SudokuGameUnitTest {
    @Test
    fun isValid_Test_Vertical_True(){
        val sudokuGame = SudokuGame(null)
        SudokuGame.mainBoard[0][0].value = 3
        SudokuGame.selectedRow = 1
        SudokuGame.selectedCol = 1
        Assert.assertEquals(true, sudokuGame.isValidNumber(2))
    }
    @Test
    fun isValid_Test_Vertical_False(){
        val sudokuGame = SudokuGame(null)
        SudokuGame.mainBoard[0][0].value = 5
        SudokuGame.selectedRow = 1
        SudokuGame.selectedCol = 1
        Assert.assertEquals(false, sudokuGame.isValidNumber(5))
    }
    @Test
    fun isValid_Test_Horizontal_True(){
        val sudokuGame = SudokuGame(null)
        SudokuGame.mainBoard[0][0].value = 7
        SudokuGame.selectedRow = 0
        SudokuGame.selectedCol = 1
        Assert.assertEquals(true, sudokuGame.isValidNumber(1))
    }
    @Test
    fun isValid_Test_Horizontal_False(){
        val sudokuGame = SudokuGame(null)
        SudokuGame.mainBoard[0][0].value = 8
        SudokuGame.selectedRow = 0
        SudokuGame.selectedCol = 2
        Assert.assertEquals(false, sudokuGame.isValidNumber(8))
    }
    @Test
    fun isValid_Test_InSquare_True(){
        val sudokuGame = SudokuGame(null)
        SudokuGame.mainBoard[1][0].value = 6
        SudokuGame.selectedRow =2
        SudokuGame.selectedCol = 1
        Assert.assertEquals(true, sudokuGame.isValidNumber(4))
    }
    @Test
    fun isValid_Test_InSquare_False(){
        val sudokuGame = SudokuGame(null)
        SudokuGame.mainBoard[1][0].value = 2
        SudokuGame.selectedRow = 2
        SudokuGame.selectedCol = 1
        Assert.assertEquals(false, sudokuGame.isValidNumber(2))
    }
}