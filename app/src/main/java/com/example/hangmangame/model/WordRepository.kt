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
    }

    fun getStoredWords(): List<Word> {
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
        val randomIndex = (0 until storedWords.size).random()
        return storedWords[randomIndex]
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

    private fun handleSharedPreferencesException(exception: Exception) {
        Log.e(
            "SharedPreferencesError",
            "An error occurred with SharedPreferences: ${exception.message}"
        )
    }
}