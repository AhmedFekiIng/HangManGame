package com.example.hangmangame.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class WordRepository(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    companion object {
        const val PREF_NAME = "HangmanPreferences"
        const val GAME_COUNT_KEY = "gameCount"
        const val VICTORY_COUNT_KEY = "victoryCount"
        const val ATTEMPTS_LEFT_KEY = "attemptsLeft"
    }

    private fun getStoredWords(): List<Word> {
        return listOf(
            Word("Hello"),
            Word("Amazing"),
            Word("Difficult"),
            Word("Interview"),
            Word("Mobile"),
            Word("Android"),
            Word("Always"),
            Word("Payxpert"),
            Word("Challenge")
        )
    }

    fun getRandomWord(): Word {
        val storedWords = getStoredWords()
        if (storedWords.isEmpty()) {
            throw NoSuchElementException("No words available.")
        }
        val randomIndex = (storedWords.indices).random()
        return storedWords[randomIndex]
    }

    fun getPartialWord(word: Word, correctGuesses: Map<Int, Char>): String {
        val partialWord = StringBuilder()
        for (index in word.value.indices) {
            val char = word.value[index].lowercaseChar()
            if (correctGuesses.containsKey(index) && correctGuesses[index] == char) {
                partialWord.append(char)
            } else {
                partialWord.append('_').append(' ')
            }
        }
        return partialWord.toString()
    }

    fun getGameCount(): Int {
        return try {
            sharedPreferences.getInt(GAME_COUNT_KEY, 0)
        } catch (e: ClassCastException) {
            handleSharedPreferencesException(e)
            0
        } catch (e: Exception) {
            handleSharedPreferencesException(e)
            0
        }
    }

    fun resetGameCount() {
        try {
            sharedPreferences.edit().putInt(GAME_COUNT_KEY, 0).apply()
        } catch (e: Exception) {
            handleSharedPreferencesException(e)
        }
    }

    fun resetVictoryCount() {
        try {
            sharedPreferences.edit().putInt(VICTORY_COUNT_KEY, 0).apply()
        } catch (e: Exception) {
            handleSharedPreferencesException(e)
        }
    }

    fun getVictoryCount(): Int {
        return try {
            sharedPreferences.getInt(VICTORY_COUNT_KEY, 0)
        } catch (e: Exception) {
            handleSharedPreferencesException(e)
            0
        }
    }

    fun getAttemptsLeft(): Int {
        return try {
            sharedPreferences.getInt(ATTEMPTS_LEFT_KEY, 10)
        } catch (e: Exception) {
            handleSharedPreferencesException(e)
            10
        }
    }

    fun incrementVictoryCount() {
        try {
            val currentCount = sharedPreferences.getInt(VICTORY_COUNT_KEY, 0)
            sharedPreferences.edit().putInt(VICTORY_COUNT_KEY, currentCount + 1).apply()
        } catch (e: Exception) {
            handleSharedPreferencesException(e)
        }
    }

    fun incrementGameCount() {
        try {
            val currentCount = sharedPreferences.getInt(GAME_COUNT_KEY, 0)
            sharedPreferences.edit().putInt(GAME_COUNT_KEY, currentCount + 1).apply()
        } catch (e: Exception) {
            handleSharedPreferencesException(e)
        }
    }


    fun decrementAttemptsLeft() {
        try {
            val currentAttempts = sharedPreferences.getInt(ATTEMPTS_LEFT_KEY, 10)
            sharedPreferences.edit().putInt(ATTEMPTS_LEFT_KEY, currentAttempts - 1).apply()
        } catch (e: Exception) {
            handleSharedPreferencesException(e)
        }
    }

    fun resetAttemptsLeft() {
        try {
            sharedPreferences.edit().putInt(ATTEMPTS_LEFT_KEY, 10).apply()
        } catch (e: Exception) {
            handleSharedPreferencesException(e)
        }
    }
}

private fun handleSharedPreferencesException(exception: Exception) {
    Log.e(
        "SharedPreferencesError",
        "An error occurred with SharedPreferences: ${exception.message}"
    )
}