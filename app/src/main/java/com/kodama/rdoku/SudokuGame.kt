package com.kodama.rdoku

class SudokuGame{
    companion object{
        /*var mainBoard: Array<Array<Int>> = arrayOf(
            Array<Int>(9){0},
            Array<Int>(9){0},
            Array<Int>(9){0},
            Array<Int>(9){0},
            Array<Int>(9){0},
            Array<Int>(9){0},
            Array<Int>(9){0},
            Array<Int>(9){0},
            Array<Int>(9){0})*/
        var mainBoard: Array<Array<SudokuCell>> = arrayOf(
            Array(9){SudokuCell(0,0,0)},
            Array(9){SudokuCell(0,0,0)},
            Array(9){SudokuCell(0,0,0)},
            Array(9){SudokuCell(0,0,0,)},
            Array(9){SudokuCell(0,0,0)},
            Array(9){SudokuCell(0,0,0)},
            Array(9){SudokuCell(0,0,0)},
            Array(9){SudokuCell(0,0,0)},
            Array(9){SudokuCell(0,0,0)},
        )

        var selectedRow: Int = -1
        var selectedCol: Int = -1
    }

    fun getSelectedRow(): Int = selectedRow
    fun getSelectedCol(): Int = selectedCol

    fun setSelectedRow(number: Int) {
        selectedRow = number
    }

    fun setSelectedCol(number: Int){
        selectedCol = number
    }

    fun setNumberBoard(number: Int){
        if(selectedRow != -1 && getSelectedCol() != -1 && !mainBoard[selectedRow - 1][selectedCol - 1].locked){
            if(mainBoard[selectedRow - 1][selectedCol - 1].value == number){
                mainBoard[selectedRow - 1][selectedCol - 1].value = 0
            }
            else{
                mainBoard[selectedRow - 1][selectedCol - 1].value = number
            }
        }
    }

    fun getBoard():Array<Array<SudokuCell>> = mainBoard

    fun isValidNumber(number: Int):Boolean{
        for(i: Int in 0 until 9){
            // Check column
            if(i != selectedCol && mainBoard[selectedRow][i].value == number){
                return false
            }
            // Check row
            if(i != selectedRow && mainBoard[i][selectedCol].value == number){
                false
            }
        }

        // Just for better readability
        val row = selectedRow
        val col = selectedCol

        for(i: Int in (row - (row % 3)) until (row - (row % 3) + 3)){
            for(j: Int in (col - (col % 3)) until (col - (col % 3) + 3)){
                if(i != row && j != col && mainBoard[i][j].value == number){
                    return false
                }
            }
        }
        return true
    }
}