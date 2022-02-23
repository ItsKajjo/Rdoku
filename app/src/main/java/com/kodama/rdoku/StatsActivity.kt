package com.kodama.rdoku

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kodama.rdoku.adapters.StatsGameModesAdapter
import com.kodama.rdoku.gamelogic.BestTimeManager
import com.kodama.rdoku.model.GameDifficulty
import com.kodama.rdoku.model.GameType
import kotlin.math.round

class StatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setTitle(R.string.stats_title)

        val rcViewBestTimes = findViewById<RecyclerView>(R.id.rcviewStatsBestTime)

        rcViewBestTimes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcViewBestTimes.adapter = StatsGameModesAdapter(this)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val gamesStarted = prefs.getInt("games_started", 0)
        val gamesCompleted = prefs.getInt("games_completed", 0)

        findViewById<TextView>(R.id.textStatsGamesStartedValue).text = gamesStarted.toString()
        findViewById<TextView>(R.id.textStatsGamesCompletedValue).text = gamesCompleted.toString()

        findViewById<TextView>(R.id.textStatsCompRateValue).text  = if(gamesCompleted != 0)
            getString(R.string.stats_comp_rate_placeholder, round(gamesCompleted.toFloat() / gamesStarted.toFloat() * 100f).toString())
        else
            "0%"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.stats_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_stats_reset_all ->{
                resetShowAlertDialog()
                true
            }
            android.R.id.home ->{
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun resetShowAlertDialog(){
        val builder = AlertDialog.Builder(this).setTitle(R.string.reset_stats_alert_title)
            .setMessage(R.string.reset_stats_alert_message)
            .setPositiveButton(R.string.alert_dialog_yes){ _, _ ->
                resetAllStats()
                this.recreate()
            }
            .setNegativeButton(R.string.alert_dialog_no){ dialog, _ ->
                dialog.dismiss()
            }

        val alert = builder.create()
        alert.show()

        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getColor(R.color.alert_dialog_button_text))
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getColor(R.color.alert_dialog_button_text))
    }

    private fun resetAllStats(){
        val bestTimeManager = BestTimeManager(this)

        // reset 9x9 mode
        bestTimeManager.saveBestTime(0, 0, GameDifficulty.Easy, GameType.classic_9x9)
        bestTimeManager.saveBestTime(0, 0, GameDifficulty.Moderate, GameType.classic_9x9)
        bestTimeManager.saveBestTime(0, 0, GameDifficulty.Hard, GameType.classic_9x9)

        // reset 6x6 mode
        bestTimeManager.saveBestTime(0, 0, GameDifficulty.Easy, GameType.classic_6x6)
        bestTimeManager.saveBestTime(0, 0, GameDifficulty.Moderate, GameType.classic_6x6)
        bestTimeManager.saveBestTime(0, 0, GameDifficulty.Hard, GameType.classic_6x6)

        // rest overall stats
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()

        editor.putInt("games_started", 0)
        editor.putInt("games_completed", 0)
        editor.apply()
    }
}