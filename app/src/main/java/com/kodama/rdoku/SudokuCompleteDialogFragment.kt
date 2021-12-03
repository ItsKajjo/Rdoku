package com.kodama.rdoku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.kodama.rdoku.gamelogic.GameDifficulty

class SudokuCompleteDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rview = inflater.inflate(R.layout.layout_sudoku_complete, container, false)

        val timeMinutes = requireArguments().getLong("time_minutes")
        val timeSeconds = requireArguments().getLong("time_seconds")

        val bestTimeMinutes = requireArguments().getLong("best_time_minutes")
        val bestTimeSeconds = requireArguments().getLong("best_time_seconds")

        val gameDifficulty = requireArguments().getSerializable("game_difficulty") as GameDifficulty


        val tvTimeComplete = rview.findViewById<TextView>(R.id.tvTimeComplete)
        val tvBestTime = rview.findViewById<TextView>(R.id.tvBestTime)
        val tvDifficulty = rview.findViewById<TextView>(R.id.tvDifficultyComplete)

        // using padStart to get leading zeros in minutes and seconds
        tvTimeComplete.text = getString(R.string.complete_time_placeholder,
            timeMinutes.toString().padStart(2, '0'),
            timeSeconds.toString().padStart(2, '0'))

        tvBestTime.text = getString(R.string.best_time_placeholder,
            bestTimeMinutes.toString().padStart(2, '0'),
            bestTimeSeconds.toString().padStart(2, '0'))

        tvDifficulty.text = when(gameDifficulty){
            GameDifficulty.Easy -> getString(R.string.difficulty_easy)
            GameDifficulty.Moderate -> getString(R.string.difficulty_moderate)
            GameDifficulty.Hard -> getString(R.string.difficulty_hard)
        }

        val btnRestart: Button = rview.findViewById(R.id.btnCompleteRestart)
        btnRestart.setOnClickListener {
            this.dismiss()

            (activity as GameActivity).startGame()
        }
        return rview
    }
}