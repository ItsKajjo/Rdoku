package com.kodama.rdoku

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.widget.Button

class GameActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        var game_difficulty: Int = 1
        val bundle: Bundle? = intent.extras
        if(bundle != null){
            game_difficulty = bundle.getInt("game_difficulty")
        }
    }
}