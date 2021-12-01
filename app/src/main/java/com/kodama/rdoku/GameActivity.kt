package com.kodama.rdoku

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.*
import androidx.preference.PreferenceManager
import com.kodama.rdoku.customview.SudokuBoardView
import com.kodama.rdoku.gamelogic.BestTimeManager
import com.kodama.rdoku.gamelogic.GameDifficulty
import com.kodama.rdoku.gamelogic.SudokuGame
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async

class GameActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        val bundle: Bundle? = intent.extras

        if(bundle != null){
            gameDifficulty = bundle.getSerializable("game_difficulty") as GameDifficulty
            cmTimer = findViewById(R.id.cmTimer)
            startGame()
        }

        sudokuBoard = findViewById(R.id.sudokuBoard)

        initPrefs()
    }

    lateinit var cmTimer: Chronometer

    private var job = Job()
    private val sudokuGame = SudokuGame(this)
    private lateinit var sudokuBoard: SudokuBoardView
    private lateinit var gameDifficulty: GameDifficulty


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.restart_menu ->{
                startGame()
                true
            }
            R.id.about_menu -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onContextItemSelected(item)
            }
        }
    }

    private fun startGame(){
        when(gameDifficulty){
            GameDifficulty.Easy -> {
                GlobalScope.async{
                    sudokuGame.generateBoard(32)
                }
                findViewById<TextView>(R.id.tvDifficulty).text = getString(R.string.difficulty_easy)
            }

            GameDifficulty.Moderate -> {
                GlobalScope.async{
                    sudokuGame.generateBoard(27)
                }
                findViewById<TextView>(R.id.tvDifficulty).text = getString(R.string.difficulty_moderate)
            }

            GameDifficulty.Hard ->{
                sudokuGame.setHardBoard()
                findViewById<TextView>(R.id.tvDifficulty).text = getString(R.string.difficulty_hard)
            }
        }

        cmTimer.base = SystemClock.elapsedRealtime()
        cmTimer.start()

        enableGameKeyboard(true)
    }

    override fun onDestroy() {
        cmTimer.stop()
        job.cancel()
        super.onDestroy()
    }

    fun onBtnNumberClick(view: View){
        val number: Int = when(view.id){
            R.id.btnOne -> 1
            R.id.btnTwo -> 2
            R.id.btnThree -> 3
            R.id.btnFour -> 4
            R.id.btnFive -> 5
            R.id.btnSix -> 6
            R.id.btnSeven -> 7
            R.id.btnEight -> 8
            R.id.btnNine -> 9
            else -> 0
        }
        sudokuGame.setNumberBoard(number)

        checkForComplete()

        hideFullyUsedNumber()
        sudokuBoard.invalidate()
    }

    private fun completeDialog(cmTimer: Chronometer){
        cmTimer.stop()

        // get min and sec from chronometer
        val seconds = (SystemClock.elapsedRealtime() - cmTimer.base) / 1000 % 60
        val minutes = (SystemClock.elapsedRealtime() - cmTimer.base) / 1000 / 60 % 60
        val sec = seconds.toString().padStart(2, '0')
        val min = minutes.toString().padStart(2, '0')

        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_sudoku_complete)


        val bestTimeManager = BestTimeManager(this)

        var bestTime = bestTimeManager.getBestTime(gameDifficulty)
        var bestSeconds = bestTime.first
        var bestMinutes = bestTime.second

        if(minutes <= bestMinutes && seconds < bestSeconds || bestMinutes == 0L && bestSeconds == 0L){
            bestTimeManager.saveBestTime(seconds, minutes, gameDifficulty)
        }


        bestTime = bestTimeManager.getBestTime(gameDifficulty)
        bestSeconds = bestTime.first
        bestMinutes = bestTime.second

        val tvBestTime: TextView = dialog.findViewById(R.id.tvBestTime)
        val tvTimeComplete: TextView = dialog.findViewById(R.id.tvTimeComplete)

        dialog.findViewById<TextView>(R.id.tvDifficultyComplete).text = when(gameDifficulty){
            GameDifficulty.Easy -> getString(R.string.difficulty_easy)
            GameDifficulty.Moderate -> getString(R.string.difficulty_moderate)
            GameDifficulty.Hard -> getString(R.string.difficulty_hard)
        }


        // Show time and best time only if timer is enabled
        val appPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        dialog.setOnShowListener{
            if(appPreferences.getBoolean("enable_timer", true)){
                tvBestTime.text = getString(R.string.best_time_placeholder,
                    bestTime.second.toString().padStart(2, '0'),
                    bestTime.first.toString().padStart(2, '0'))

                tvTimeComplete.text = getString(R.string.complete_time_placeholder, min, sec)
            }
            else{
                tvBestTime.visibility = View.GONE
                tvTimeComplete.visibility = View.GONE
                dialog.findViewById<TextView>(R.id.tvBestTimeHeader).visibility = View.GONE
                dialog.findViewById<TextView>(R.id.tvTimeCompleteHeader).visibility = View.GONE
            }
        }

        val btnRestart = dialog.findViewById(R.id.btnCompleteRestart) as Button
        btnRestart.setOnClickListener {
            startGame()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initPrefs(){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        if(!prefs.getBoolean("enable_timer", true)){
            findViewById<LinearLayout>(R.id.linearLayoutTimer).visibility = View.GONE
        }
    }

    fun onBtnEraseClick(view: View){
        val row = SudokuGame.selectedRow - 1
        val col = SudokuGame.selectedCol - 1
        val num = SudokuGame.mainBoard[row][col].value

        sudokuGame.eraseNumber()

        hideFullyUsedNumber()
        sudokuBoard.invalidate()
    }

    fun onBtnHintClick(view: View){
        val row = SudokuGame.selectedRow - 1
        val col = SudokuGame.selectedCol - 1

        sudokuGame.useHint()

        val num = SudokuGame.mainBoard[row][col].value
        hideFullyUsedNumber()
        checkForComplete()

        sudokuBoard.invalidate()
    }

    fun onDbgBtnSolveClick(view: View){
        sudokuGame.debugSolve()
        enableGameKeyboard(false)
        sudokuBoard.invalidate()
    }

    private fun checkForComplete(){
        if(sudokuGame.checkForComplete()){
            completeDialog(cmTimer)
        }
    }

    private fun hideFullyUsedNumber(){
        for(i in 1..9){
            val button: Button = when(i){
                1 -> findViewById(R.id.btnOne)
                2 -> findViewById(R.id.btnTwo)
                3 -> findViewById(R.id.btnThree)
                4 -> findViewById(R.id.btnFour)
                5 -> findViewById(R.id.btnFive)
                6 -> findViewById(R.id.btnSix)
                7 -> findViewById(R.id.btnSeven)
                8 -> findViewById(R.id.btnEight)
                9 -> findViewById(R.id.btnNine)
                else -> return
            }
            if(sudokuGame.numberUsedNineTimes(i)){
                button.visibility = View.INVISIBLE
            }
            else{
                if(button.visibility == View.INVISIBLE){
                    button.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun enableGameKeyboard(isVisible: Boolean){
        val visibility: Int = if(isVisible){
            View.VISIBLE
        } else{
            View.INVISIBLE
        }

        val gameKeyboard = findViewById<LinearLayout>(R.id.gameKeyboard)
        for(i in 0 until gameKeyboard.childCount){
            val btn = gameKeyboard.getChildAt(i) as Button
            btn.visibility = visibility
        }
    }
}