package com.kodama.rdoku

class SudokuGame{

    private var selectedRow: Int = -1
    private var selectedCol: Int = -1

    fun getSelectedRow() : Int = selectedRow
    fun getSelectedCol() : Int = selectedCol

    fun setSelectedRow(row: Int){
        selectedRow = row
    }
    fun setSelectedCol(col: Int){
        selectedCol = col
    }
}