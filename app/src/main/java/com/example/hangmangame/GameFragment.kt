package com.example.hangmangame

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.hangmangame.domain.GameState
import com.example.hangmangame.domain.HangmanGame
import com.example.hangmangame.model.Word
import com.example.hangmangame.model.WordRepository
import com.example.hangmangame.presentation.GameController

class GameFragment : Fragment() {

    private lateinit var nbVictoriesTextView: TextView
    private lateinit var nbGamesTextView: TextView
    private lateinit var instructionsTextView: TextView
    private lateinit var partialWordTextView: TextView
    private lateinit var guessEditText: EditText
    private lateinit var guessButton: Button
    private lateinit var resetButton: Button

    private val gameController: GameController by lazy {
        GameController(HangmanGame(Word("")), WordRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.game_fragment, container, false)

        nbVictoriesTextView = view.findViewById(R.id.nbVictoriesTextView)
        nbGamesTextView = view.findViewById(R.id.nbGamesTextView)
        instructionsTextView = view.findViewById(R.id.instructionsTextView)
        partialWordTextView = view.findViewById(R.id.partialWordTextView)
        guessEditText = view.findViewById(R.id.guessEditText)
        guessButton = view.findViewById(R.id.guessButton)
        resetButton = view.findViewById(R.id.resetButton)

        initializeGame()

        return view
    }

    private fun initializeGame() {
        Log.d("HangmanCall", "onCreateView")
        gameController.startNewGame()
        setupUI()
    }


    private fun setupUI() {
        guessButton.setOnClickListener {
            val guess = guessEditText.text.toString().takeIf { it.isNotEmpty() }
            guess?.let { makeGuess(it) }
        }

        resetButton.setOnClickListener {
            Log.d("HangmanCall", "resetButton")
            resetGame()
        }

        updateUI()
    }

    private fun makeGuess(guess: String) {
        val gameState = gameController.makeGuess(guess)

        if (gameState == GameState.WIN || gameState == GameState.LOSE) {
            showGameResult(gameState)
        }
        updateUI()

    }

    private fun resetGame() {
        gameController.resetGameCount()
        gameController.resetVictoryCount()
        gameController.startNewGame()
        updateUI()
    }

    private fun updateUI() {
        nbVictoriesTextView.text =
            getString(R.string.nb_victories, gameController.getVictoryCount())
        nbGamesTextView.text = getString(R.string.nb_games, gameController.getGameCount())
        instructionsTextView.text =
            getString(R.string.instructions, gameController.getAttemptsLeft())

        partialWordTextView.text = gameController.getPartialWord()

        guessEditText.text.clear()
    }

    private fun showGameResult(gameState: GameState) {
        if (gameState == GameState.LOSE) {
            showDialog(isWinner = false)
        } else if (gameState == GameState.WIN) {
            showDialog(isWinner = true)
        }
    }

    private fun showDialog(isWinner: Boolean) {
        val message = if (isWinner) {
            "Congratulations! You won!"
        } else {
            "You have lost this game!"
        }
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage(message + " The word was ${gameController.getActualWord()}")
            .setCancelable(false)
            .setPositiveButton("NEW GAME") { dialog, _ ->
                gameController.startNewGame()
                updateUI()
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle(if (isWinner) "Congratulations" else "Game Over")
        alert.show()
    }
}