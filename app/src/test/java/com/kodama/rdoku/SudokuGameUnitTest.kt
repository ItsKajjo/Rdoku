package com.kodama.rdoku

import org.junit.Assert
import org.junit.Test

class SudokuGameUnitTest {
    @Test
    fun isValid_Test_Vertical_True(){
        val sudokuGame = SudokuGame()
        sudokuGame.getBoard()[0][0] = 3
        sudokuGame.getSelectedRow = 1
        sudokuGame.getSelectedCol = 1
        Assert.assertEquals(true, sudokuGame.isValidNumber(2))
    }
    @Test
    fun isValid_Test_Vertical_False(){
        val sudokuGame = SudokuGame()
        sudokuGame.getBoard()[0][0] = 5
        sudokuGame.getSelectedRow = 1
        sudokuGame.getSelectedCol = 1
        Assert.assertEquals(false, sudokuGame.isValidNumber(5))
    }
    @Test
    fun isValid_Test_Horizontal_True(){
        val sudokuGame = SudokuGame()
        sudokuGame.getBoard()[0][0] = 7
        sudokuGame.getSelectedRow = 0
        sudokuGame.getSelectedCol = 1
        Assert.assertEquals(true, sudokuGame.isValidNumber(1))
    }
    @Test
    fun isValid_Test_Horizontal_False(){
        val sudokuGame = SudokuGame()
        sudokuGame.getBoard()[0][0] = 8
        sudokuGame.getSelectedRow = 0
        sudokuGame.getSelectedCol = 2
        Assert.assertEquals(false, sudokuGame.isValidNumber(8))
    }
    @Test
    fun isValid_Test_InSquare_True(){
        val sudokuGame = SudokuGame()
        sudokuGame.getBoard()[1][0] = 6
        sudokuGame.getSelectedRow = 2
        sudokuGame.getSelectedCol = 1
        Assert.assertEquals(true, sudokuGame.isValidNumber(4))
    }
    @Test
    fun isValid_Test_InSquare_False(){
        val sudokuGame = SudokuGame()
        sudokuGame.getBoard()[1][0] = 2
        sudokuGame.getSelectedRow = 2
        sudokuGame.getSelectedCol = 1
        Assert.assertEquals(false, sudokuGame.isValidNumber(2))
    }
}