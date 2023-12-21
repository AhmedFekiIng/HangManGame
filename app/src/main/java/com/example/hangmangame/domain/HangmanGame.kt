package com.example.hangmangame.domain

import com.example.hangmangame.model.Word
import com.example.hangmangame.model.WordRepository

class HangmanGame(private var word: Word)
{

    private var actualWord: String = word.value
    private var correctGuesses: MutableList<Char> = mutableListOf()
    private var incorrectAttempts: Int = 0
    private val maxAttempts: Int = 10

    fun getActualWord(): String {
        return actualWord
    }

    fun makeGuess(word: String, wordRepository: WordRepository): GameState {
        if (isWordGuessed(word)) {
            wordRepository.incrementVictoryCount()
            wordRepository.incrementGameCount()
            wordRepository.resetAttemptsLeft()
            return GameState.WIN
        }

        incorrectAttempts++
        if (incorrectAttempts >= maxAttempts) {
            wordRepository.incrementGameCount()
            wordRepository.resetAttemptsLeft()
            return GameState.LOSE
        }
        correctGuesses.clear()
        for ((index, letter) in word.withIndex()) {
            val actualLetter = this.word.value.getOrNull(index)?.lowercaseChar()
            if (actualLetter != null && (actualLetter.equals(letter.lowercaseChar(), ignoreCase = true) || actualLetter == letter.lowercaseChar())) {
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
        correctGuesses.clear()
        incorrectAttempts = 0
        return GameState.IN_PROGRESS
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