package com.kodama.rdoku

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.kodama.rdoku.gamelogic.BestTimeManager
import com.kodama.rdoku.model.GameDifficulty

class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_activity_settings)

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)

        findViewById<Button>(R.id.btnPrefResetEasy).setOnClickListener {
            val bestTimeManager = BestTimeManager(this)
            bestTimeManager.saveBestTime(0, 0, GameDifficulty.Easy)
            Toast.makeText(this, getString(R.string.easy_reset_success), Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnPrefResetModerate).setOnClickListener {
            val bestTimeManager = BestTimeManager(this)
            bestTimeManager.saveBestTime(0, 0, GameDifficulty.Moderate)
            Toast.makeText(this, getString(R.string.moderate_reset_success), Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnPrefResetHard).setOnClickListener {
            val bestTimeManager = BestTimeManager(this)
            bestTimeManager.saveBestTime(0, 0, GameDifficulty.Hard)
            Toast.makeText(this, getString(R.string.hard_reset_success), Toast.LENGTH_SHORT).show()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when(key){
            "app_theme" -> {
                val prefs = sharedPreferences?.getString(key, "1")

                when (prefs?.toInt()) {
                    1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    3 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }
}