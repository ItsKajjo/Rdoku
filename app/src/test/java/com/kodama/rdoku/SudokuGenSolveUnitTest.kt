package com.kodama.rdoku

import com.kodama.rdoku.gamelogic.SudokuCell
import com.kodama.rdoku.gamelogic.SudokuGenSolve
import org.junit.Assert
import org.junit.Test

class SudokuGenSolveUnitTest {
    @Test
    fun isSudokuFeasibleTest_True(){
        val sudokuGenSolve = SudokuGenSolve()
        sudokuGenSolve.gameBoard = arrayOf(
            arrayOf(SudokuCell(1), SudokuCell(7), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(2), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(6), SudokuCell(8), SudokuCell(0)),
            arrayOf(SudokuCell(8), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(5), SudokuCell(4), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(6), SudokuCell(0)),
            arrayOf(SudokuCell(3), SudokuCell(5), SudokuCell(0), SudokuCell(6), SudokuCell(0), SudokuCell(7), SudokuCell(1), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(2), SudokuCell(1), SudokuCell(0), SudokuCell(0), SudokuCell(3), SudokuCell(0), SudokuCell(4)),
            arrayOf(SudokuCell(5), SudokuCell(0), SudokuCell(3), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(8)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(9), SudokuCell(4), SudokuCell(0), SudokuCell(0), SudokuCell(7)),
        )
        Assert.assertEquals(true, sudokuGenSolve.isSudokuFeasible())
    }

    @Test
    fun isSudokuFeasible_False(){
        val sudokuGenSolve = SudokuGenSolve()
        sudokuGenSolve.gameBoard = arrayOf(
            arrayOf(SudokuCell(1), SudokuCell(7), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(2), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(6), SudokuCell(8), SudokuCell(0)),
            arrayOf(SudokuCell(8), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(5), SudokuCell(4), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(6), SudokuCell(0)),
            arrayOf(SudokuCell(3), SudokuCell(5), SudokuCell(0), SudokuCell(6), SudokuCell(0), SudokuCell(7), SudokuCell(1), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(2), SudokuCell(1), SudokuCell(0), SudokuCell(0), SudokuCell(3), SudokuCell(0), SudokuCell(4)),
            arrayOf(SudokuCell(5), SudokuCell(0), SudokuCell(3), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(8)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(9), SudokuCell(4), SudokuCell(4), SudokuCell(0), SudokuCell(7)),
        )
        Assert.assertEquals(false, sudokuGenSolve.isSudokuFeasible())
    }

    @Test
    fun solve_Test_True(){
        val sudokuGenSolve = SudokuGenSolve()
        sudokuGenSolve.gameBoard = arrayOf(
            arrayOf(SudokuCell(1), SudokuCell(7), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(2), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(6), SudokuCell(8), SudokuCell(0)),
            arrayOf(SudokuCell(8), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(5), SudokuCell(4), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(6), SudokuCell(0)),
            arrayOf(SudokuCell(3), SudokuCell(5), SudokuCell(0), SudokuCell(6), SudokuCell(0), SudokuCell(7), SudokuCell(1), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(2), SudokuCell(1), SudokuCell(0), SudokuCell(0), SudokuCell(3), SudokuCell(0), SudokuCell(4)),
            arrayOf(SudokuCell(5), SudokuCell(0), SudokuCell(3), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(8)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(9), SudokuCell(4), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
        )
        Assert.assertEquals(true, sudokuGenSolve.solve().first)
    }

    @Test
    fun solve_Test_False(){
        val sudokuGenSolve = SudokuGenSolve()
        sudokuGenSolve.gameBoard = arrayOf(
            arrayOf(SudokuCell(1), SudokuCell(7), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(2), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(6), SudokuCell(8), SudokuCell(0)),
            arrayOf(SudokuCell(8), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(5), SudokuCell(4), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(6), SudokuCell(0)),
            arrayOf(SudokuCell(3), SudokuCell(5), SudokuCell(0), SudokuCell(6), SudokuCell(0), SudokuCell(7), SudokuCell(1), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(2), SudokuCell(1), SudokuCell(0), SudokuCell(0), SudokuCell(3), SudokuCell(0), SudokuCell(4)),
            arrayOf(SudokuCell(5), SudokuCell(0), SudokuCell(3), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(8)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(9), SudokuCell(4), SudokuCell(0), SudokuCell(0), SudokuCell(7)),
        )
        Assert.assertEquals(false, sudokuGenSolve.solve().first)
    }

    @Test
    fun isSudokuUnique_Test_True(){
        val sudokuGenSolve = SudokuGenSolve()
        sudokuGenSolve.gameBoard = arrayOf(
            arrayOf(SudokuCell(1), SudokuCell(7), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(2), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(6), SudokuCell(8), SudokuCell(0)),
            arrayOf(SudokuCell(8), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(5), SudokuCell(4), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(6), SudokuCell(0)),
            arrayOf(SudokuCell(3), SudokuCell(5), SudokuCell(0), SudokuCell(6), SudokuCell(0), SudokuCell(7), SudokuCell(1), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(2), SudokuCell(1), SudokuCell(0), SudokuCell(0), SudokuCell(3), SudokuCell(0), SudokuCell(4)),
            arrayOf(SudokuCell(5), SudokuCell(0), SudokuCell(3), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(8)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(9), SudokuCell(4), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
        )

        Assert.assertEquals(true, sudokuGenSolve.isSudokuUnique())
    }

    @Test
    fun isSudokuUnique_Test_False(){
        val sudokuGenSolve = SudokuGenSolve()
        sudokuGenSolve.gameBoard = arrayOf(
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(9), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(9), SudokuCell(0), SudokuCell(4), SudokuCell(6), SudokuCell(7)),
            arrayOf(SudokuCell(9), SudokuCell(0), SudokuCell(4), SudokuCell(1), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(2), SudokuCell(5)),
            arrayOf(SudokuCell(0), SudokuCell(0), SudokuCell(9), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(8)),
            arrayOf(SudokuCell(8), SudokuCell(7), SudokuCell(0), SudokuCell(0), SudokuCell(6), SudokuCell(0), SudokuCell(0), SudokuCell(1), SudokuCell(4)),
            arrayOf(SudokuCell(6), SudokuCell(0), SudokuCell(0), SudokuCell(6), SudokuCell(0), SudokuCell(0), SudokuCell(3), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(7), SudokuCell(4), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(2), SudokuCell(9), SudokuCell(0), SudokuCell(1)),
            arrayOf(SudokuCell(2), SudokuCell(9), SudokuCell(8), SudokuCell(0), SudokuCell(3), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
            arrayOf(SudokuCell(0), SudokuCell(1), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0), SudokuCell(0)),
        )

        Assert.assertEquals(false, sudokuGenSolve.isSudokuUnique())
    }

    @Test
    fun generating_Test(){
        val sudokuGenSolve = SudokuGenSolve()

        sudokuGenSolve.generate(32)
        val unique = sudokuGenSolve.isSudokuUnique()
        val feasible = sudokuGenSolve.isSudokuFeasible()
        val solvable = sudokuGenSolve.solve().first

        Assert.assertEquals(true, unique && feasible && solvable)
    }
}