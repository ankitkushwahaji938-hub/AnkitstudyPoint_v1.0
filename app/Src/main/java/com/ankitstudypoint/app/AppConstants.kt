package com.ankitstudypoint.app

object AppConstants {

    // Base URL
    const val BASE_URL = "https://ankitstudypoint.blogspot.com"

    // Navigation URLs
    const val HOME_URL      = "https://ankitstudypoint.blogspot.com/"
    const val NOTES_URL     = "https://ankitstudypoint.blogspot.com/search/label/Notes"
    const val QUIZ_URL      = "https://ankitstudypoint.blogspot.com/p/pharmasist-exam-quiz.html"
    const val YOUTUBE_URL   = "https://m.youtube.com/@rxvibeak"

    // Study Timer
    const val STUDY_TIMER_URL = "https://ankitstudypoint.blogspot.com/p/study-timer-3.html"

    // Pharma Tools
    const val EXPIRY_CHECKER_URL    = "https://ankitstudypoint.blogspot.com/p/medicine-expiry-date-checker-is-my.html"
    const val IV_DRIP_URL           = "https://ankitstudypoint.blogspot.com/p/iv-drip-rate-calculator-drops-per.html"
    const val TABLET_CALC_URL       = "https://ankitstudypoint.blogspot.com/p/tablet-strength-calculator-how-many.html"
    const val PHARMA_TOOLS_HUB_URL  = "https://ankitstudypoint.blogspot.com/p/pharma-tool.html"
    const val DILUTION_CALC_URL     = "https://ankitstudypoint.blogspot.com/p/dilution-calculator.html"
    const val ABBR_FINDER_URL       = "https://ankitstudypoint.blogspot.com/p/medical-abbreviation-finder-tool.html"
    const val BMI_CALC_URL          = "https://ankitstudypoint.blogspot.com/p/bmi-calculator.html"
    const val ABOUT_URL             = "https://ankitstudypoint.blogspot.com/p/ankitstudypoint.html"

    // Notifications
    const val NOTIFICATION_CHANNEL_ID   = "asp_notifications"
    const val NOTIFICATION_CHANNEL_NAME = "Ankit Study Point"
    const val WORK_TAG_SYNC             = "asp_sync_work"

    // SharedPreferences Keys
    const val PREF_DARK_MODE        = "dark_mode"
    const val PREF_BOOKMARKS        = "bookmarks"
    const val PREF_PROGRESS_DATA    = "progress_data"
    const val PREF_LAST_VISIT       = "last_visit"
    const val PREF_TOTAL_TIME       = "total_study_time"
    const val PREF_PAGES_VISITED    = "pages_visited"
    const val PREF_QUIZ_SCORE       = "quiz_high_score"

    // External domains that should open in browser
    val EXTERNAL_DOMAINS = setOf(
        "facebook.com",
        "twitter.com",
        "instagram.com",
        "wa.me",
        "whatsapp.com"
    )

    // Internal domains to load inside WebView
    val INTERNAL_DOMAINS = setOf(
        "ankitstudypoint.blogspot.com",
        "ankitkushwahaji938-hub.github.io",
        "m.youtube.com",
        "youtube.com"
    )
}
