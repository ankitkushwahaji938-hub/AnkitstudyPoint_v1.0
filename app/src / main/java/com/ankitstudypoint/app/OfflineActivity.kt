package com.ankitstudypoint.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ankitstudypoint.app.databinding.ActivityOfflineBinding
import com.ankitstudypoint.app.utils.NetworkUtils

class OfflineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOfflineBinding
    private val handler = Handler(Looper.getMainLooper())

    // Auto-retry every 5 seconds
    private val retryRunnable = object : Runnable {
        override fun run() {
            if (NetworkUtils.isNetworkAvailable(this@OfflineActivity)) {
                goOnline()
            } else {
                handler.postDelayed(this, 5000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfflineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRetry.setOnClickListener {
            checkAndRetry()
        }
    }

    private fun checkAndRetry() {
        binding.btnRetry.isEnabled = false
        binding.btnRetry.text = "Checking..."

        if (NetworkUtils.isNetworkAvailable(this)) {
            goOnline()
        } else {
            binding.btnRetry.isEnabled = true
            binding.btnRetry.text = "🔄 Retry"
        }
    }

    private fun goOnline() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(retryRunnable, 5000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(retryRunnable)
    }
}
