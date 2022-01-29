package com.kodama.rdoku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.kodama.rdoku.adapters.GameModesAdapter
import com.kodama.rdoku.model.GameDifficulty
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    lateinit var rcView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSettings()

        rcView = findViewById(R.id.rcViewMain)

        rcView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rcView.adapter = GameModesAdapter(this)
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rcView)
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
}