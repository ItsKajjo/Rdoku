package com.kodama.rdoku

class SudokuCell(row: Int, col: Int, value: Int, locked: Boolean = false) {
    val row: Int = row
    val col: Int = col
    var value: Int = value

    // Cell with generated value is locked, so that the user can't change it value
    val locked: Boolean = locked
}