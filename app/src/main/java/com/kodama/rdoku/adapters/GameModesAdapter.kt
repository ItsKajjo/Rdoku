package com.kodama.rdoku.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.kodama.rdoku.GameActivity
import com.kodama.rdoku.R
import com.kodama.rdoku.model.GameDifficulty
import com.kodama.rdoku.model.GameMode
import com.kodama.rdoku.model.GameType
import java.io.Serializable

class GameModesAdapter(val context: Context) : RecyclerView.Adapter<GameModesAdapter.GameModesViewHolder>() {

    private val gameModesList: List<GameMode> = listOf(
        GameMode(context.getString(R.string.classic_9x9), GameType.classic_9x9, context.getString(R.string.classic_9x9_description), R.drawable.ic_classic_9x9),
        GameMode(context.getString(R.string.classic_6x6), GameType.classic_6x6, context.getString(R.string.classic_6x6_description), R.drawable.ic_classic_6x6)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameModesViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_game_mode, parent, false)

        return GameModesViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameModesAdapter.GameModesViewHolder, position: Int) {
        holder.title.text = gameModesList[position].title
        holder.image.setImageResource(gameModesList[position].image)
        holder.description.text = gameModesList[position].description

        holder.btnPlay.setOnClickListener {
            val intent = Intent(context, GameActivity::class.java)

            when(holder.difficultySpinner.selectedItemId){
                0L -> intent.putExtra("game_difficulty", GameDifficulty.Easy as Serializable)
                1L -> intent.putExtra("game_difficulty", GameDifficulty.Moderate as Serializable)
                2L -> intent.putExtra("game_difficulty", GameDifficulty.Hard as Serializable)
                else -> intent.putExtra("game_difficulty", GameDifficulty.Easy as Serializable)
            }

            intent.putExtra("game_type", gameModesList[position].gameType as Serializable)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = gameModesList.size

    inner class GameModesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.tvModeTitle)
        val image: ImageView = itemView.findViewById(R.id.ivGameMode)
        val description: TextView = itemView.findViewById(R.id.cardModesTextDescription)

        val difficultySpinner: Spinner = itemView.findViewById(R.id.spinnerGameDifficulty)
        val btnPlay: Button = itemView.findViewById(R.id.btnCardPlay)
    }
}