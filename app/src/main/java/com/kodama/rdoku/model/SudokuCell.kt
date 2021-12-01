package com.kodama.rdoku.model

class SudokuCell(value: Int, locked: Boolean = false, wrong: Boolean = false) {
    var value: Int = value

    // The cell with generated value is locked to prevent the user from changing its value
    var locked: Boolean = locked

    var wrong: Boolean = wrong
}