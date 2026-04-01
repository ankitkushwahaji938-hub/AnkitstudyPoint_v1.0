package com.ankitstudypoint.app.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.ankitstudypoint.app.AppConstants

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("asp_prefs", Context.MODE_PRIVATE)

    fun isDarkMode(): Boolean = prefs.getBoolean(AppConstants.PREF_DARK_MODE, false)
    fun setDarkMode(enabled: Boolean) = prefs.edit().putBoolean(AppConstants.PREF_DARK_MODE, enabled).apply()

    fun getPagesVisited(): Int = prefs.getInt(AppConstants.PREF_PAGES_VISITED, 0)
    fun incrementPagesVisited() = prefs.edit().putInt(AppConstants.PREF_PAGES_VISITED, getPagesVisited() + 1).apply()

    fun getTotalStudyTime(): Long = prefs.getLong(AppConstants.PREF_TOTAL_TIME, 0L)
    fun addStudyTime(millis: Long) = prefs.edit().putLong(AppConstants.PREF_TOTAL_TIME, getTotalStudyTime() + millis).apply()

    fun getLastVisit(): Long = prefs.getLong(AppConstants.PREF_LAST_VISIT, 0L)
    fun updateLastVisit() = prefs.edit().putLong(AppConstants.PREF_LAST_VISIT, System.currentTimeMillis()).apply()

    fun getQuizHighScore(): Int = prefs.getInt(AppConstants.PREF_QUIZ_SCORE, 0)
    fun setQuizHighScore(score: Int) {
        if (score > getQuizHighScore()) {
            prefs.edit().putInt(AppConstants.PREF_QUIZ_SCORE, score).apply()
        }
    }

    fun clearAll() = prefs.edit().clear().apply()
}
