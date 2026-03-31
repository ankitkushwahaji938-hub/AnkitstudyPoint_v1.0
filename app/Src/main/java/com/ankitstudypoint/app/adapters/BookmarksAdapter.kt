package com.ankitstudypoint.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ankitstudypoint.app.R
import com.ankitstudypoint.app.utils.BookmarkManager
import java.text.SimpleDateFormat
import java.util.*

class BookmarksAdapter(
    private val onItemClick: (BookmarkManager.Bookmark) -> Unit,
    private val onItemDelete: (BookmarkManager.Bookmark) -> Unit
) : RecyclerView.Adapter<BookmarksAdapter.BookmarkViewHolder>() {

    private var bookmarks: List<BookmarkManager.Bookmark> = emptyList()

    inner class BookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.bookmark_title)
        val url: TextView = itemView.findViewById(R.id.bookmark_url)
        val date: TextView = itemView.findViewById(R.id.bookmark_date)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.bookmark_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false)
        return BookmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val bookmark = bookmarks[position]
        holder.title.text = bookmark.title
        holder.url.text = bookmark.url
        holder.date.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(bookmark.timestamp))

        holder.itemView.setOnClickListener { onItemClick(bookmark) }
        holder.deleteBtn.setOnClickListener { onItemDelete(bookmark) }
    }

    override fun getItemCount() = bookmarks.size

    fun updateList(list: List<BookmarkManager.Bookmark>) {
        bookmarks = list
        notifyDataSetChanged()
    }
}
