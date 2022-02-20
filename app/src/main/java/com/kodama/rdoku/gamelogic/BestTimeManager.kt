package com.kodama.rdoku.gamelogic

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.kodama.rdoku.model.GameDifficulty
import com.kodama.rdoku.model.GameType

class BestTimeManager(private val context: Context) {

    fun saveBestTime(seconds: Long, minutes: Long, gameDifficulty: GameDifficulty, gameType: GameType){
        val prefs = context.getSharedPreferences("best_time", AppCompatActivity.MODE_PRIVATE)
        val editor = prefs.edit()

        // TODO make it better, tf is this spaghetti
        when(gameType){
            GameType.classic_9x9 ->{
                when(gameDifficulty){
                    GameDifficulty.Easy ->{
                        editor.putLong("best_seconds_easy_9x9", seconds)
                        editor.putLong("best_minutes_easy_9x9", minutes)
                    }

                    GameDifficulty.Moderate ->{
                        editor.putLong("best_seconds_moderate_9x9", seconds)
                        editor.putLong("best_minutes_moderate_9x9", minutes)
                    }

                    GameDifficulty.Hard ->{
                        editor.putLong("best_seconds_hard_9x9", seconds)
                        editor.putLong("best_minutes_hard_9x9", minutes)
                    }
                }
            }
            GameType.classic_6x6 ->{
                when(gameDifficulty){
                    GameDifficulty.Easy ->{
                        editor.putLong("best_seconds_easy_6x6", seconds)
                        editor.putLong("best_minutes_easy_6x6", minutes)
                    }

                    GameDifficulty.Moderate ->{
                        editor.putLong("best_seconds_moderate_6x6", seconds)
                        editor.putLong("best_minutes_moderate_6x6", minutes)
                    }

                    GameDifficulty.Hard ->{
                        editor.putLong("best_seconds_hard_6x6", seconds)
                        editor.putLong("best_minutes_hard_6x6", minutes)
                    }
                }
            }
        }
        editor.apply()
    }

    /**
     * Gets best time from sharedPreferences
     * @return First: seconds, Second: Minutes
     * **/
    fun getBestTime(gameDifficulty: GameDifficulty, gameType: GameType):Pair<Long, Long>{
        val prefs = context.getSharedPreferences("best_time", AppCompatActivity.MODE_PRIVATE)
        var bestSeconds = 0L
        var bestMinutes = 0L

        // TODO and make it better too
        when(gameType){
            GameType.classic_9x9 ->{
                when(gameDifficulty){
                    GameDifficulty.Easy ->{
                        bestSeconds = prefs.getLong("best_seconds_easy_9x9", 0L)
                        bestMinutes = prefs.getLong("best_minutes_easy_9x9", 0L)
                    }

                    GameDifficulty.Moderate ->{
                        bestSeconds = prefs.getLong("best_seconds_moderate_9x9", 0L)
                        bestMinutes = prefs.getLong("best_minutes_moderate_9x9", 0L)
                    }

                    GameDifficulty.Hard ->{
                        bestSeconds = prefs.getLong("best_seconds_hard_9x9", 0L)
                        bestMinutes = prefs.getLong("best_minutes_hard_9x9", 0L)
                    }
                }
            }

            GameType.classic_6x6 ->{
                when(gameDifficulty){
                    GameDifficulty.Easy ->{
                        bestSeconds = prefs.getLong("best_seconds_easy_6x6", 0L)
                        bestMinutes = prefs.getLong("best_minutes_easy_6x6", 0L)
                    }

                    GameDifficulty.Moderate ->{
                        bestSeconds = prefs.getLong("best_seconds_moderate_6x6", 0L)
                        bestMinutes = prefs.getLong("best_minutes_moderate_6x6", 0L)
                    }

                    GameDifficulty.Hard ->{
                        bestSeconds = prefs.getLong("best_seconds_hard_6x6", 0L)
                        bestMinutes = prefs.getLong("best_minutes_hard_6x6", 0L)
                    }
                }
            }
        }
        return Pair(bestSeconds, bestMinutes)
    }

}