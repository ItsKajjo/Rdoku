package com.kodama.rdoku

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

class GameActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        var game_difficulty = 1
        val bundle: Bundle? = intent.extras
        if(bundle != null){
            game_difficulty = bundle.getInt("game_difficulty")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.settings_menu -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.restart_menu ->{
                TODO("Restart method is not exists yet")
            }
            R.id.about_menu ->
            {
                TODO("About Activity is not exists yet")
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
}