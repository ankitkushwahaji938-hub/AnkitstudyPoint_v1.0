package com.ankitstudypoint.app.fragments

import com.ankitstudypoint.app.AppConstants

class HomeFragment : WebViewFragment() {
    override val pageUrl = AppConstants.HOME_URL
    companion object { const val TAG = "HomeFragment" }
}

class NotesFragment : WebViewFragment() {
    override val pageUrl = AppConstants.NOTES_URL
    companion object { const val TAG = "NotesFragment" }
}

class QuizFragment : WebViewFragment() {
    override val pageUrl = AppConstants.QUIZ_URL
    companion object { const val TAG = "QuizFragment" }
}

class YouTubeFragment : WebViewFragment() {
    override val pageUrl = AppConstants.YOUTUBE_URL
    companion object { const val TAG = "YouTubeFragment" }
}
