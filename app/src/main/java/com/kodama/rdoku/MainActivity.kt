package com.kodama.rdoku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.kodama.rdoku.model.GameDifficulty
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSettings()
    }

    private fun initSettings(){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        when(prefs.getString("app_theme", "1")?.toInt()){
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            3 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun onSettingsClick(view: View){
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun onAboutClick(view: View){
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    fun onNewGameClick(view: View){
        val intent: Intent = Intent(this, GameActivity::class.java)
        val spinner: Spinner = findViewById(R.id.spinner_game_difficulty)

        when(spinner.selectedItemId){
            0L -> intent.putExtra("game_difficulty", GameDifficulty.Easy as Serializable)
            1L -> intent.putExtra("game_difficulty", GameDifficulty.Moderate as Serializable)
            2L -> intent.putExtra("game_difficulty", GameDifficulty.Hard as Serializable)
            else -> intent.putExtra("game_difficulty", GameDifficulty.Easy as Serializable)
        }
        startActivityForResult(intent, RESULT_OK)
    }

}