package com.ankitstudypoint.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.ankitstudypoint.app.AppConstants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BookmarkManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("asp_bookmarks", Context.MODE_PRIVATE)
    private val gson = Gson()

    data class Bookmark(
        val url: String,
        val title: String,
        val timestamp: Long,
        val favicon: String? = null
    )

    fun addBookmark(bookmark: Bookmark) {
        val bookmarks = getAllBookmarks().toMutableList()
        if (!isBookmarked(bookmark.url)) {
            bookmarks.add(0, bookmark) // Add to top
            saveBookmarks(bookmarks)
        }
    }

    fun removeBookmark(url: String) {
        val bookmarks = getAllBookmarks().toMutableList()
        bookmarks.removeAll { it.url == url }
        saveBookmarks(bookmarks)
    }

    fun isBookmarked(url: String): Boolean {
        return getAllBookmarks().any { it.url == url }
    }

    fun getAllBookmarks(): List<Bookmark> {
        val json = prefs.getString(AppConstants.PREF_BOOKMARKS, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<Bookmark>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun clearAllBookmarks() {
        prefs.edit().remove(AppConstants.PREF_BOOKMARKS).apply()
    }

    fun getBookmarkCount(): Int = getAllBookmarks().size

    private fun saveBookmarks(bookmarks: List<Bookmark>) {
        prefs.edit()
            .putString(AppConstants.PREF_BOOKMARKS, gson.toJson(bookmarks))
            .apply()
    }
}
