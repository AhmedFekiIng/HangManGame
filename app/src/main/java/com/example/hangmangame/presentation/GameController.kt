package com.example.hangmangame.presentation

import android.util.Log
import com.example.hangmangame.domain.GameState
import com.example.hangmangame.domain.HangmanGame
import com.example.hangmangame.model.Word
import com.example.hangmangame.model.WordRepository

class GameController(private val hangmanGame: HangmanGame, private val wordRepository: WordRepository) {

    fun startNewGame(): GameState {
        val newWord = wordRepository.getRandomWord()
        return hangmanGame.startNewGame(newWord)
    }

    fun makeGuess(letter: Char): GameState {
        return hangmanGame.makeGuess(letter, wordRepository)
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

    fun getVictoryCount(): Int {
        return wordRepository.getVictoryCount()
    }

    fun getAttemptsLeft(): Int {
        return wordRepository.getAttemptsLeft()
    }

    fun getPartialWord(): String {
        val actualWord = hangmanGame.getActualWord()
        val correctGuesses = hangmanGame.getCorrectGuesses()
        Log.d("Hangman", "Actual Word: $actualWord")
        Log.d("Hangman", "Correct Guesses: $correctGuesses")
        return wordRepository.getPartialWord(Word(actualWord), correctGuesses)
    }

}