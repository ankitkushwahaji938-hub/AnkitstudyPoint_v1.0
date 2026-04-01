package com.ankitstudypoint.app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ankitstudypoint.app.AppConstants
import com.ankitstudypoint.app.R
import com.ankitstudypoint.app.WebViewActivity
import com.ankitstudypoint.app.adapters.ToolsAdapter
import com.ankitstudypoint.app.databinding.FragmentToolsBinding
import com.ankitstudypoint.app.models.ToolItem

class ToolsFragment : Fragment() {

    private var _binding: FragmentToolsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToolsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolsGrid()
    }

    private fun setupToolsGrid() {
        val tools = listOf(
            ToolItem(
                id = 1,
                name = "Pharma Tools Hub",
                description = "All tools in one place",
                emoji = "🧰",
                url = AppConstants.PHARMA_TOOLS_HUB_URL,
                colorRes = R.color.tool_blue,
                isFeatured = true
            ),
            ToolItem(
                id = 2,
                name = "Medicine Expiry Checker",
                description = "Check if medicine is expired",
                emoji = "💊",
                url = AppConstants.EXPIRY_CHECKER_URL,
                colorRes = R.color.tool_red
            ),
            ToolItem(
                id = 3,
                name = "IV Drip Rate Calculator",
                description = "Calculate drops per minute",
                emoji = "💉",
                url = AppConstants.IV_DRIP_URL,
                colorRes = R.color.tool_teal
            ),
            ToolItem(
                id = 4,
                name = "Tablet Strength Calculator",
                description = "Calculate tablet dosage",
                emoji = "⚖️",
                url = AppConstants.TABLET_CALC_URL,
                colorRes = R.color.tool_orange
            ),
            ToolItem(
                id = 5,
                name = "Dilution Calculator",
                description = "Concentration dilutions",
                emoji = "🧪",
                url = AppConstants.DILUTION_CALC_URL,
                colorRes = R.color.tool_purple
            ),
            ToolItem(
                id = 6,
                name = "Medical Abbreviation Finder",
                description = "Decode medical terms",
                emoji = "🔍",
                url = AppConstants.ABBR_FINDER_URL,
                colorRes = R.color.tool_green
            ),
            ToolItem(
                id = 7,
                name = "BMI Calculator",
                description = "Body Mass Index",
                emoji = "📊",
                url = AppConstants.BMI_CALC_URL,
                colorRes = R.color.tool_indigo
            ),
            ToolItem(
                id = 8,
                name = "Study Timer",
                description = "Focus & productivity timer",
                emoji = "⏱️",
                url = AppConstants.STUDY_TIMER_URL,
                colorRes = R.color.tool_yellow
            ),
            ToolItem(
                id = 9,
                name = "About Ankit Study Point",
                description = "About this platform",
                emoji = "ℹ️",
                url = AppConstants.ABOUT_URL,
                colorRes = R.color.tool_gray
            )
        )

        val adapter = ToolsAdapter(tools) { tool ->
            openTool(tool)
        }

        binding.toolsRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (tools[position].isFeatured) 2 else 1
                    }
                }
            }
            this.adapter = adapter
            layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        }

        binding.toolsRecyclerView.scheduleLayoutAnimation()
    }

    private fun openTool(tool: ToolItem) {
        val intent = Intent(requireContext(), WebViewActivity::class.java).apply {
            putExtra(WebViewActivity.EXTRA_URL, tool.url)
            putExtra(WebViewActivity.EXTRA_TITLE, tool.name)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ToolsFragment"
    }
}
