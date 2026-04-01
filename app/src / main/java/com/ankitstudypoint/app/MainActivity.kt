package com.ankitstudypoint.app

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ankitstudypoint.app.databinding.ActivityMainBinding
import com.ankitstudypoint.app.fragments.*
import com.ankitstudypoint.app.utils.NetworkUtils
import com.ankitstudypoint.app.utils.PreferenceManager
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PreferenceManager
    private var currentFragment: Fragment? = null

    // Fragment stack for each tab
    private val fragmentTags = mapOf(
        R.id.nav_home     to HomeFragment.TAG,
        R.id.nav_notes    to NotesFragment.TAG,
        R.id.nav_quiz     to QuizFragment.TAG,
        R.id.nav_tools    to ToolsFragment.TAG,
        R.id.nav_youtube  to YouTubeFragment.TAG
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefManager = PreferenceManager(this)

        // Apply dark mode setting
        val nightMode = if (prefManager.isDarkMode()) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupBottomNavigation()
        setupFab()
        requestNotificationPermission()

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment(), HomeFragment.TAG, R.id.nav_home)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment(), HomeFragment.TAG, R.id.nav_home)
                    updateTitle("Ankit Study Point")
                    true
                }
                R.id.nav_notes -> {
                    loadFragment(NotesFragment(), NotesFragment.TAG, R.id.nav_notes)
                    updateTitle("Study Notes")
                    true
                }
                R.id.nav_quiz -> {
                    loadFragment(QuizFragment(), QuizFragment.TAG, R.id.nav_quiz)
                    updateTitle("Quiz")
                    true
                }
                R.id.nav_tools -> {
                    loadFragment(ToolsFragment(), ToolsFragment.TAG, R.id.nav_tools)
                    updateTitle("Pharma Tools")
                    true
                }
                R.id.nav_youtube -> {
                    loadFragment(YouTubeFragment(), YouTubeFragment.TAG, R.id.nav_youtube)
                    updateTitle("YouTube")
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String, navId: Int) {
        val fm = supportFragmentManager
        val existing = fm.findFragmentByTag(tag)

        val transaction = fm.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)

        // Hide all fragments
        fm.fragments.forEach { transaction.hide(it) }

        if (existing != null) {
            transaction.show(existing)
            currentFragment = existing
        } else {
            transaction.add(R.id.fragment_container, fragment, tag)
            currentFragment = fragment
        }

        transaction.commit()

        // Update FAB visibility
        binding.fabTimer.show()
    }

    private fun setupFab() {
        binding.fabTimer.setOnClickListener {
            openStudyTimer()
        }

        binding.fabTimer.setOnLongClickListener {
            val options = arrayOf("📚 Study Timer", "🎯 Quick Quiz", "🔖 Bookmarks")
            AlertDialog.Builder(this)
                .setTitle("Quick Access")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> openStudyTimer()
                        1 -> openUrl(AppConstants.QUIZ_URL, "Quick Quiz")
                        2 -> openBookmarks()
                    }
                }.show()
            true
        }
    }

    private fun openStudyTimer() {
        val intent = Intent(this, StudyTimerActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_up, R.anim.fade_out)
    }

    private fun openBookmarks() {
        startActivity(Intent(this, BookmarksActivity::class.java))
    }

    fun openUrl(url: String, title: String = "") {
        val intent = Intent(this, WebViewActivity::class.java).apply {
            putExtra(WebViewActivity.EXTRA_URL, url)
            putExtra(WebViewActivity.EXTRA_TITLE, title)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out)
    }

    private fun updateTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_bookmarks -> {
                openBookmarks()
                true
            }
            R.id.menu_share -> {
                shareApp()
                true
            }
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.menu_offline -> {
                checkOfflineStatus()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Ankit Study Point - D.Pharmacy Study App")
            putExtra(
                Intent.EXTRA_TEXT,
                "📚 Study D.Pharmacy with Ankit Study Point!\n\n" +
                "✅ Study Notes  ✅ Pharma Tools  ✅ Quizzes\n\n" +
                "Visit: https://ankitstudypoint.blogspot.com\n\n" +
                "Download the app and start studying smarter!"
            )
        }
        startActivity(Intent.createChooser(shareIntent, "Share Ankit Study Point"))
    }

    private fun checkOfflineStatus() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Snackbar.make(binding.root, "You are offline. Some content may not load.", Snackbar.LENGTH_LONG)
                .setAction("Retry") { recreate() }
                .show()
        } else {
            Toast.makeText(this, "✓ Connected to internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notifications enabled! 🔔", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        val fragment = currentFragment
        if (fragment is WebViewFragment && fragment.canGoBack()) {
            fragment.goBack()
        } else if (binding.bottomNavigation.selectedItemId != R.id.nav_home) {
            binding.bottomNavigation.selectedItemId = R.id.nav_home
        } else {
            showExitDialog()
        }
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit App?")
            .setMessage("Are you sure you want to exit Ankit Study Point?")
            .setPositiveButton("Exit") { _, _ -> finish() }
            .setNegativeButton("Stay") { dialog, _ -> dialog.dismiss() }
            .setIcon(R.drawable.ic_exit)
            .show()
    }

    companion object {
        const val NOTIFICATION_PERMISSION_CODE = 101
    }
}
