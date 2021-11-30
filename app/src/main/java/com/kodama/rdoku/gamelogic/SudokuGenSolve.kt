package com.kodama.rdoku.gamelogic

import kotlin.random.Random

class SudokuGenSolve{
    // For better performance
    private val boardSubIndex: Array<Array<Pair<Int,Int>>> = arrayOf(
        arrayOf(Pair(0,0), Pair(0,1), Pair(0,2), Pair(1,0), Pair(1,1), Pair(1,2), Pair(2,0), Pair(2,1), Pair(2,2)),
        arrayOf(Pair(0,3), Pair(0,4), Pair(0,5), Pair(1,3), Pair(1,4), Pair(1,5), Pair(2,3), Pair(2,4), Pair(2,5)),
        arrayOf(Pair(0,6), Pair(0,7), Pair(0,8), Pair(1,6), Pair(1,7), Pair(1,8), Pair(2,6), Pair(2,7), Pair(2,8)),
        arrayOf(Pair(3,0), Pair(3,1), Pair(3,2), Pair(4,0), Pair(4,1), Pair(4,2), Pair(5,0), Pair(5,1), Pair(5,2)),
        arrayOf(Pair(3,3), Pair(3,4), Pair(3,5), Pair(4,3), Pair(4,4), Pair(4,5), Pair(5,3), Pair(5,4), Pair(5,5)),
        arrayOf(Pair(3,6), Pair(3,7), Pair(3,8), Pair(4,6), Pair(4,7), Pair(4,8), Pair(5,6), Pair(5,7), Pair(5,8)),
        arrayOf(Pair(6,0), Pair(6,1), Pair(6,2), Pair(7,0), Pair(7,1), Pair(7,2), Pair(8,0), Pair(8,1), Pair(8,2)),
        arrayOf(Pair(6,3), Pair(6,4), Pair(6,5), Pair(7,3), Pair(7,4), Pair(7,5), Pair(8,3), Pair(8,4), Pair(8,5)),
        arrayOf(Pair(6,6), Pair(6,7), Pair(6,8), Pair(7,6), Pair(7,7), Pair(7,8), Pair(8,6), Pair(8,7), Pair(8,8))
    )
    private val squareIndexes: Array<Array<Int>> = arrayOf(
        arrayOf(0,0,0,1,1,1,2,2,2),
        arrayOf(0,0,0,1,1,1,2,2,2),
        arrayOf(0,0,0,1,1,1,2,2,2),
        arrayOf(3,3,3,4,4,4,5,5,5),
        arrayOf(3,3,3,4,4,4,5,5,5),
        arrayOf(3,3,3,4,4,4,5,5,5),
        arrayOf(6,6,6,7,7,7,8,8,8),
        arrayOf(6,6,6,7,7,7,8,8,8),
        arrayOf(6,6,6,7,7,7,8,8,8)
    )
    var gameBoard: Array<Array<SudokuCell>> = arrayOf(
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

    /**
     * Checks if board feasible to solve or not
     **/
    fun isSudokuFeasible():Boolean{
        for(row: Int in 0 until 9){
            for(col: Int in 0 until 9){
                var candidates: Array<Int> = Array(10) {0}

                for(i: Int in 0 until 9){
                    candidates[gameBoard[i][col].value] += 1
                }
                if(!feasible(candidates)){
                    return false
                }

                candidates = Array(10) {0}
                for(i: Int in 0 until 9){
                    candidates[gameBoard[row][i].value] += 1
                }
                if(!feasible(candidates)){
                    return false
                }

                candidates = Array(10) {0}
                var squareIndex: Int = squareIndexes[row][col]
                for(i: Int in 0 until 9){
                    val cell: Pair<Int, Int> = boardSubIndex[squareIndex][i]
                    if(cell.first != row && cell.second != col){
                        candidates[gameBoard[cell.first][cell.second].value] += 1
                    }
                }
                if(!feasible(candidates)){
                    return false
                }
            }
        }
        return true
    }

    /**
     * Checks if board has a unique solution or not
     * @return Is sudoku board unique or not
     * */
    fun isSudokuUnique():Boolean{
        var originalBoard: Array<Array<SudokuCell>> = gameBoard
        var isUnique: Boolean = false
        if(testUniqueness() == BoardRate.Unique){
            isUnique = true
        }
        gameBoard = originalBoard
        return isUnique
    }

    /**
     * Generating a valid sudoku board with unique solution
     * */
    fun generate(spots: Int, numberOfTries: Int = 950000):Triple<Boolean, Array<Array<SudokuCell>>, Int>{
        resetBoard()
        var tries: Int = 0

        while(tries < numberOfTries){
            if(gen(spots)){
                if(isSudokuUnique()){
                    return Triple(true, gameBoard, tries)
                }
            }

            tries += 1
            resetBoard()
        }
        return Triple(false, gameBoard, tries)
    }


    private fun resetBoard(){
        gameBoard = arrayOf(
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

    /**
     * Solves a sudoku board
     * **/
    fun solve(): Pair<Boolean, Array<Array<SudokuCell>>> {
        var prevX: Int = 0
        var prevY: Int = 0
        var prevCand: Array<Int>? = null
        var prevCandCardinal: Int = 10

        for(row: Int in 0 until 9){
            for(col: Int in 0 until 9){
                // Is this spot unused
                if(gameBoard[row][col].value == 0){
                    var candidates: Array<Int> = arrayOf(0,1,2,3,4,5,6,7,8,9)

                    for(i: Int in 0 until 9){
                        candidates[gameBoard[i][col].value] = 0
                        candidates[gameBoard[row][i].value] = 0
                    }

                    var squareIndex: Int = squareIndexes[row][col]
                    for(c: Int in 0 until 9){
                        val cell: Pair<Int, Int> = boardSubIndex[squareIndex][c]
                        candidates[gameBoard[cell.first][cell.second].value] = 0
                    }

                    var cardinalityCandidates: Int = 0
                    for(i: Int in 1..9){
                        if(candidates[i] != 0){
                            cardinalityCandidates += 1
                        }
                    }

                    if(cardinalityCandidates < prevCandCardinal){
                        prevCandCardinal = cardinalityCandidates
                        prevCand = candidates
                        prevX = col
                        prevY = row
                    }
                }
            }
        }

        // Finished?
        if(prevCandCardinal == 10){
            return Pair(true, gameBoard)
        }
        // Couldn't find a solution?
        if(prevCandCardinal == 0){
            return Pair(false, gameBoard)
        }

        // Try elements
        for(i: Int in 1..9){
            if(prevCand != null && prevCand[i] != 0){
                gameBoard[prevY][prevX].value = prevCand[i]
                if(solve().first){
                    return Pair(true, gameBoard)
                }
            }
        }

        // Restore to original state
        gameBoard[prevY][prevX].value = 0
        return Pair(false, gameBoard)
    }


    private fun gen(spots: Int):Boolean{
        for(i: Int in 0 until spots){
            var xRand: Int
            var yRand: Int

            do{
                xRand = Random.nextInt(0,9)
                yRand = Random.nextInt(0, 9)
            }while(gameBoard[yRand][xRand].value != 0)

            var candidates: Array<Int> = arrayOf(0,1,2,3,4,5,6,7,8,9)

            // Remove used in vertical and horizontal directions
            for(i: Int in 0 until 9){
                candidates[gameBoard[i][xRand].value] = 0
                candidates[gameBoard[yRand][i].value] = 0
            }


            var squareIndex: Int = squareIndexes[yRand][xRand]
            for(c: Int in 0 until 9){
                val cell: Pair<Int, Int> = boardSubIndex[squareIndex][c]
                candidates[gameBoard[cell.first][cell.second].value] = 0
            }

            var cM: Int = 0
            for(d: Int in 1 until 10){
                if(candidates[d] != 0){
                    cM += 1
                }
            }

            if(cM > 0){
                var randNum: Int = 0

                do{
                    randNum = Random.nextInt(1,10)
                }while(candidates[randNum] == 0)

                gameBoard[yRand][xRand].value = randNum
            }
            if(cM < 1){
                // Error
                return false
            }
        }
        return true
    }
    private fun testUniqueness():BoardRate{
        var xp: Int = 0
        var yp: Int = 0
        var mp: Array<Int>? = null
        var prevCandCardinality: Int = 10

        for(row: Int in 0 until 9){
            for(col: Int in 0 until 9){
                if(gameBoard[row][col].value == 0){
                    var candidates: Array<Int> = arrayOf(0,1,2,3,4,5,6,7,8,9)

                    for(i: Int in 0 until 9){
                        candidates[gameBoard[i][col].value] = 0
                        candidates[gameBoard[row][i].value] = 0
                    }


                    var squareIndex: Int = squareIndexes[row][col]
                    for(i: Int in 0 until 9){
                        val cell: Pair<Int, Int> = boardSubIndex[squareIndex][i]
                        candidates[gameBoard[cell.first][cell.second].value] = 0
                    }

                    var candCardinality: Int = 0
                    for(i: Int in 1..9){
                        if(candidates[i] != 0){
                            candCardinality += 1
                        }
                    }
                    if(candCardinality < prevCandCardinality){
                        prevCandCardinality = candCardinality
                        mp = candidates
                        xp = col
                        yp = row
                    }
                }
            }
        }

        // Finished?
        if(prevCandCardinality == 10){
            return BoardRate.Unique
        }
        // Could not find a solution?
        if(prevCandCardinality == 0){
            return BoardRate.NoSolution
        }

        // Try elements
        var success: Int = 0
        for(i: Int in 1..9){
            if(mp != null && mp[i] != 0){
                gameBoard[yp][xp].value = mp[i]

                when(testUniqueness()){
                    BoardRate.Unique -> success += 1
                    BoardRate.NotUnique -> return BoardRate.NotUnique
                    BoardRate.NoSolution -> {}
                }

                // More than one solution
                if(success > 1){
                    return BoardRate.NotUnique
                }
            }
        }

        gameBoard[yp][xp].value = 0

        return when(success){
            0 -> BoardRate.NoSolution
            1 -> BoardRate.Unique
            else -> BoardRate.NotUnique
        }
    }
    private fun feasible(candidates: Array<Int>):Boolean{
        for(i: Int in 1..9){
            if(candidates[i] > 1){
                return false
            }
        }
        return true
    }
}
