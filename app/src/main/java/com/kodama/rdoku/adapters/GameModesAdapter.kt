package com.kodama.rdoku.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
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
        GameMode(context.getString(R.string.classic_mode_9x9), GameType.classic_9x9, R.drawable.ic_standart_game_mode),
        GameMode(context.getString(R.string.killer_sudoku), GameType.killer_sudoku, R.drawable.ic_standart_game_mode)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameModesViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_game_mode, parent, false)

        return GameModesViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameModesAdapter.GameModesViewHolder, position: Int) {
        holder.title.text = gameModesList[position].title
        holder.image.setImageResource(gameModesList[position].image)

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        when(prefs.getString("last_selected_difficulty", "easy")){
            "easy" -> holder.difficultySpinner.setSelection(0)
            "moderate" -> holder.difficultySpinner.setSelection(1)
            "hard" -> holder.difficultySpinner.setSelection(2)
        }

        holder.btnPlay.setOnClickListener {
            if(gameModesList[position].gameType == GameType.classic_9x9){
                val intent: Intent = Intent(context, GameActivity::class.java)

                when(holder.difficultySpinner.selectedItemId){
                    0L -> intent.putExtra("game_difficulty", GameDifficulty.Easy as Serializable)
                    1L -> intent.putExtra("game_difficulty", GameDifficulty.Moderate as Serializable)
                    2L -> intent.putExtra("game_difficulty", GameDifficulty.Hard as Serializable)
                    else -> intent.putExtra("game_difficulty", GameDifficulty.Easy as Serializable)
                }

                intent.putExtra("game_type", gameModesList[position].gameType as Serializable)
                context.startActivity(intent)

            }
            else{
                Toast.makeText(context, context.getString(R.string.in_dev), Toast.LENGTH_SHORT).show()
            }
        }

        holder.difficultySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val editor = prefs.edit()

                when(position){
                    0 -> editor.putString("last_selected_difficulty", "easy")
                    1 -> editor.putString("last_selected_difficulty", "moderate")
                    2 -> editor.putString("last_selected_difficulty", "hard")

                    else -> editor.putString("last_selected_difficulty", "easy")
                }
                editor.apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun getItemCount(): Int = gameModesList.size

    inner class GameModesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.tvModeTitle)
        val image: ImageView = itemView.findViewById(R.id.ivGameMode)
        val difficultySpinner: Spinner = itemView.findViewById(R.id.spinnerGameDifficulty)
        val btnPlay: Button = itemView.findViewById(R.id.btnCardPlay)
    }
}