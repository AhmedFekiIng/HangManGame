package com.example.hangmangame.domain

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

    fun makeGuess(letter: Char, wordRepository: WordRepository): GameState {
        if (isLetterGuessed(letter)) {
            return GameState.ALREADY_GUESSED
        }

        if (word.value.contains(letter, true)) {
            updateHiddenWord(letter)
            correctGuesses.add(letter)

            if (isWordGuessed()) {
                wordRepository.incrementVictoryCount()
                wordRepository.resetAttemptsLeft()
                return GameState.WIN
            }
        } else {
            incorrectAttempts++
            if (incorrectAttempts >= maxAttempts) {
                wordRepository.decrementAttemptsLeft()
                return GameState.LOSE
            }
        }
        wordRepository.decrementAttemptsLeft()

        return GameState.IN_PROGRESS
    }

    fun startNewGame(newWord: Word): GameState {
        word = newWord
        actualWord = word.value
        hiddenWord = CharArray(word.value.length) { '_' }
        correctGuesses.clear()
        incorrectAttempts = 0
        return GameState.IN_PROGRESS
    }

    private fun isLetterGuessed(letter: Char): Boolean {
        return correctGuesses.contains(letter)
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

    private fun isWordGuessed(): Boolean {
        return String(hiddenWord) == word.value
    }
}

enum class GameState {
    WIN,
    LOSE,
    IN_PROGRESS,
    ALREADY_GUESSED
}