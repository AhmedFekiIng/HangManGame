package com.example.hangmangame.domain

import android.util.Log
import com.example.hangmangame.model.Word
import com.example.hangmangame.model.WordRepository

class HangmanGame(private var word: Word)
{

    private var actualWord: String = word.value
    private var hiddenWord: CharArray = CharArray(word.value.length) { '_' }
    private var correctGuesses: MutableList<Char> = mutableListOf()
    private var incorrectAttempts: Int = 0
    private val maxAttempts: Int = 10

    fun getActualWord(): String {
        return actualWord
    }

    fun makeGuess(word: String, wordRepository: WordRepository): GameState {
        if (isWordGuessed(word)) {
            Log.d("HangmanCall", "isWordGuessed HangmanGame")
            wordRepository.incrementVictoryCount()
            wordRepository.incrementGameCount()
            wordRepository.resetAttemptsLeft()
            return GameState.WIN
        }

        incorrectAttempts++
        if (incorrectAttempts >= maxAttempts) {
            Log.d("HangmanCall", "decrementAttemptsLeft HangmanGame")
            wordRepository.incrementGameCount()
            wordRepository.resetAttemptsLeft()
            return GameState.LOSE
        }
        correctGuesses.clear()
        for (letter in word) {

            if (this.word.value.contains(letter, true)) {
                updateHiddenWord(letter)
                correctGuesses.add(letter)
            }
        }
        wordRepository.decrementAttemptsLeft()
        return GameState.IN_PROGRESS
    }

    private fun isWordGuessed(word: String): Boolean {
        return word.equals(this.word.value, ignoreCase = true)
    }
    fun startNewGame(newWord: Word): GameState {
        word = newWord
        actualWord = word.value
        hiddenWord = CharArray(word.value.length) { '_' }
        correctGuesses.clear()
        incorrectAttempts = 0
        return GameState.IN_PROGRESS
    }

    private fun updateHiddenWord(letter: Char) {
        for (i in word.value.indices) {
            if (word.value[i].equals(letter, true)) {
                hiddenWord[i] = letter
            }
        }
    }

    fun getCorrectGuesses(): List<Char> {
        return correctGuesses
    }

}

enum class GameState {
    WIN,
    LOSE,
    IN_PROGRESS
}