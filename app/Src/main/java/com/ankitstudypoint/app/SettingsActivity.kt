package com.ankitstudypoint.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ankitstudypoint.app.databinding.ActivitySettingsBinding
import com.ankitstudypoint.app.utils.BookmarkManager
import com.ankitstudypoint.app.utils.PreferenceManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefManager: PreferenceManager
    private lateinit var bookmarkManager: BookmarkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "⚙️ Settings"

        prefManager = PreferenceManager(this)
        bookmarkManager = BookmarkManager(this)

        setupUI()
    }

    private fun setupUI() {
        // Dark mode toggle
        binding.switchDarkMode.isChecked = prefManager.isDarkMode()
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefManager.setDarkMode(isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // Stats
        val pagesVisited = prefManager.getPagesVisited()
        val totalTimeMin = prefManager.getTotalStudyTime() / 60000
        val bookmarkCount = bookmarkManager.getBookmarkCount()

        binding.tvPagesVisited.text = "$pagesVisited pages"
        binding.tvStudyTime.text = "$totalTimeMin min"
        binding.tvBookmarkCount.text = "$bookmarkCount saved"

        // Clear cache
        binding.btnClearCache.setOnClickListener {
            clearWebViewCache()
        }

        // Clear bookmarks
        binding.btnClearBookmarks.setOnClickListener {
            bookmarkManager.clearAllBookmarks()
            Toast.makeText(this, "Bookmarks cleared", Toast.LENGTH_SHORT).show()
        }

        // Reset stats
        binding.btnResetStats.setOnClickListener {
            prefManager.clearAll()
            Toast.makeText(this, "Stats reset", Toast.LENGTH_SHORT).show()
            setupUI()
        }

        // Open website
        binding.btnVisitWebsite.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.HOME_URL)))
        }

        // Rate app
        binding.btnRateApp.setOnClickListener {
            Toast.makeText(this, "Coming soon on Play Store!", Toast.LENGTH_SHORT).show()
        }

        // App version
        binding.tvVersion.text = "Version ${BuildConfig.VERSION_NAME}"
    }

    private fun clearWebViewCache() {
        val dir = cacheDir
        if (dir.exists()) dir.deleteRecursively()
        Toast.makeText(this, "Cache cleared ✓", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}
