package com.ankitstudypoint.app

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ankitstudypoint.app.adapters.BookmarksAdapter
import com.ankitstudypoint.app.databinding.ActivityBookmarksBinding
import com.ankitstudypoint.app.utils.BookmarkManager

class BookmarksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookmarksBinding
    private lateinit var bookmarkManager: BookmarkManager
    private lateinit var adapter: BookmarksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "⭐ My Bookmarks"

        bookmarkManager = BookmarkManager(this)
        setupRecyclerView()
        loadBookmarks()

        binding.btnClearAll.setOnClickListener {
            showClearConfirmDialog()
        }
    }

    private fun setupRecyclerView() {
        adapter = BookmarksAdapter(
            onItemClick = { bookmark ->
                val intent = Intent(this, WebViewActivity::class.java).apply {
                    putExtra(WebViewActivity.EXTRA_URL, bookmark.url)
                    putExtra(WebViewActivity.EXTRA_TITLE, bookmark.title)
                }
                startActivity(intent)
            },
            onItemDelete = { bookmark ->
                bookmarkManager.removeBookmark(bookmark.url)
                loadBookmarks()
                Toast.makeText(this, "Bookmark removed", Toast.LENGTH_SHORT).show()
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun loadBookmarks() {
        val bookmarks = bookmarkManager.getAllBookmarks()
        if (bookmarks.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.btnClearAll.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.btnClearAll.visibility = View.VISIBLE
            adapter.updateList(bookmarks)
        }
    }

    private fun showClearConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("Clear All Bookmarks")
            .setMessage("Are you sure you want to remove all bookmarks?")
            .setPositiveButton("Clear All") { _, _ ->
                bookmarkManager.clearAllBookmarks()
                loadBookmarks()
                Toast.makeText(this, "All bookmarks cleared", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        loadBookmarks()
    }
}
