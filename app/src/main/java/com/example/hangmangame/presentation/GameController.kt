package com.example.hangmangame.presentation

import android.util.Log
import com.example.hangmangame.domain.GameState
import com.example.hangmangame.domain.HangmanGame
import com.example.hangmangame.model.Word
import com.example.hangmangame.model.WordRepository

class GameController(private val hangmanGame: HangmanGame, private val wordRepository: WordRepository) {

    fun startNewGame(): GameState {
        val newWord = wordRepository.getRandomWord()
        wordRepository.resetAttemptsLeft()
        return hangmanGame.startNewGame(newWord)
    }

    fun makeGuess(word: String): GameState {
        return hangmanGame.makeGuess(word, wordRepository)
    }

    fun getActualWord(): String {
        return hangmanGame.getActualWord()
    }

    fun getGameCount(): Int {
        return wordRepository.getGameCount()
    }

    fun resetGameCount() {
        wordRepository.resetGameCount()
    }

    fun resetVictoryCount() {
        wordRepository.resetVictoryCount()
    }

    fun getVictoryCount(): Int {
        return wordRepository.getVictoryCount()
    }

    fun getAttemptsLeft(): Int {
        return wordRepository.getAttemptsLeft()
    }

    fun getPartialWord(): String {
        val actualWord = hangmanGame.getActualWord()
        val correctGuesses = hangmanGame.getCorrectGuesses()
        Log.d("HangmanCall", "Actual Word: $actualWord")
        return wordRepository.getPartialWord(Word(actualWord), correctGuesses)
    }

}