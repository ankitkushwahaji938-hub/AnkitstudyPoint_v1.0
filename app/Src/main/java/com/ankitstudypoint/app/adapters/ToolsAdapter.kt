package com.ankitstudypoint.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ankitstudypoint.app.R
import com.ankitstudypoint.app.models.ToolItem

class ToolsAdapter(
    private val tools: List<ToolItem>,
    private val onToolClick: (ToolItem) -> Unit
) : RecyclerView.Adapter<ToolsAdapter.ToolViewHolder>() {

    inner class ToolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.tool_card)
        val emoji: TextView = itemView.findViewById(R.id.tool_emoji)
        val name: TextView = itemView.findViewById(R.id.tool_name)
        val desc: TextView = itemView.findViewById(R.id.tool_description)
        val badge: TextView? = itemView.findViewById(R.id.tool_badge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val layout = if (viewType == VIEW_TYPE_FEATURED) R.layout.item_tool_featured else R.layout.item_tool
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ToolViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        val tool = tools[position]
        val context = holder.itemView.context

        holder.emoji.text = tool.emoji
        holder.name.text = tool.name
        holder.desc.text = tool.description

        if (tool.isFeatured) {
            holder.badge?.visibility = View.VISIBLE
            holder.badge?.text = "⭐ Featured"
        }

        // Card color
        holder.card.setCardBackgroundColor(ContextCompat.getColor(context, tool.colorRes))

        // Click
        holder.card.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(context, R.anim.scale_click)
            it.startAnimation(anim)
            onToolClick(tool)
        }
    }

    override fun getItemCount() = tools.size

    override fun getItemViewType(position: Int): Int =
        if (tools[position].isFeatured) VIEW_TYPE_FEATURED else VIEW_TYPE_NORMAL

    companion object {
        const val VIEW_TYPE_NORMAL   = 0
        const val VIEW_TYPE_FEATURED = 1
    }
}
