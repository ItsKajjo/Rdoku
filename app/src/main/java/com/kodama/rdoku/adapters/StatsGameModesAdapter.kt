package com.kodama.rdoku.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kodama.rdoku.R
import com.kodama.rdoku.gamelogic.BestTimeManager
import com.kodama.rdoku.model.GameDifficulty
import com.kodama.rdoku.model.GameMode
import com.kodama.rdoku.model.GameType

class StatsGameModesAdapter(val context: Context) : RecyclerView.Adapter<StatsGameModesAdapter.StatsGameModeViewHolder>() {
    private val gameModesList: List<GameMode> = listOf(
        GameMode(context.getString(R.string.classic_9x9), GameType.classic_9x9, R.drawable.ic_classic_9x9),
        GameMode(context.getString(R.string.classic_6x6), GameType.classic_6x6, R.drawable.ic_classic_6x6)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsGameModeViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.stats_best_card, parent, false)

        return StatsGameModeViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatsGameModeViewHolder, position: Int) {
        holder.textGameType.text = gameModesList[position].title

        val bestTimeManager = BestTimeManager(context)
        val easy = bestTimeManager.getBestTime(GameDifficulty.Easy, gameModesList[position].gameType)
        val moderate = bestTimeManager.getBestTime(GameDifficulty.Moderate, gameModesList[position].gameType)
        val hard = bestTimeManager.getBestTime(GameDifficulty.Hard, gameModesList[position].gameType)

        holder.bestEasy.text = if(!(easy.first == 0L && easy.second == 0L))
            context.getString(R.string.mmss_time_placeholder,
                easy.second.toString().padStart(2, '0'),
                easy.first.toString().padStart(2,'0'))
        else
            context.getString(R.string.no_best)

        holder.bestModerate.text = if(!(moderate.first == 0L && moderate.second == 0L))
            context.getString(R.string.mmss_time_placeholder,
            moderate.second.toString().padStart(2, '0'),
            moderate.first.toString().padStart(2,'0'))
        else
            context.getString(R.string.no_best)

        holder.bestHard.text = if(!(hard.first == 0L && hard.second == 0L))
            context.getString(R.string.mmss_time_placeholder,
            hard.second.toString().padStart(2, '0'),
            hard.first.toString().padStart(2,'0'))
        else
            context.getString(R.string.no_best)
    }

    override fun getItemCount(): Int = gameModesList.size

    inner class StatsGameModeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textGameType = itemView.findViewById<TextView>(R.id.stats_card_text_game_type)
        val bestEasy = itemView.findViewById<TextView>(R.id.stats_card_text_easy_value)
        val bestModerate = itemView.findViewById<TextView>(R.id.stats_card_text_moderate_value)
        val bestHard = itemView.findViewById<TextView>(R.id.stats_card_text_hard_value)
    }
}