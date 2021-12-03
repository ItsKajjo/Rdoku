package com.kodama.rdoku.gamelogic

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class BestTimeManager(private val context: Context) {

    fun saveBestTime(seconds: Long, minutes: Long, gameDifficulty: GameDifficulty){
        val prefs = context.getSharedPreferences("best_time", AppCompatActivity.MODE_PRIVATE)
        val editor = prefs.edit()

        when(gameDifficulty){
            GameDifficulty.Easy ->{
                editor.putLong("best_seconds_easy", seconds)
                editor.putLong("best_minutes_easy", minutes)
            }

            GameDifficulty.Moderate ->{
                editor.putLong("best_seconds_moderate", seconds)
                editor.putLong("best_minutes_moderate", minutes)
            }

            GameDifficulty.Hard ->{
                editor.putLong("best_seconds_hard", seconds)
                editor.putLong("best_minutes_hard", minutes)
            }
        }
        editor.apply()
    }

    /**
     * Get best time from sharedPreferences
     * @return First: seconds, Second: Minutes
     * **/
    fun getBestTime(gameDifficulty: GameDifficulty):Pair<Long, Long>{
        val prefs = context.getSharedPreferences("best_time", AppCompatActivity.MODE_PRIVATE)
        var bestSeconds = 0L
        var bestMinutes = 0L

        when(gameDifficulty){
            GameDifficulty.Easy ->{
                bestSeconds = prefs.getLong("best_seconds_easy", 0L)
                bestMinutes = prefs.getLong("best_minutes_easy", 0L)
            }

            GameDifficulty.Moderate ->{
                bestSeconds = prefs.getLong("best_seconds_moderate", 0L)
                bestMinutes = prefs.getLong("best_minutes_moderate", 0L)
            }

            GameDifficulty.Hard ->{
                bestSeconds = prefs.getLong("best_seconds_hard", 0L)
                bestMinutes = prefs.getLong("best_minutes_hard", 0L)
            }
        }
        return Pair(bestSeconds, bestMinutes)
    }

}