package com.ankitstudypoint.app

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.ankitstudypoint.app.databinding.ActivityStudyTimerBinding

class StudyTimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudyTimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "⏱️ Study Timer"

        setupWebView()
    }

    private fun setupWebView() {
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = false
                cacheMode = WebSettings.LOAD_DEFAULT
                mediaPlaybackRequiresUserGesture = false
                userAgentString = "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36"
            }

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    binding.progressBar.visibility = View.VISIBLE
                }
                override fun onPageFinished(view: WebView, url: String) {
                    binding.progressBar.visibility = View.GONE
                    // Inject full-screen CSS for timer
                    loadUrl("""javascript:(function(){
                        document.body.style.margin='0';
                        document.body.style.padding='10px';
                        document.documentElement.style.height='100%';
                        var header = document.querySelector('header, #header, .header');
                        if(header) header.style.display='none';
                        var footer = document.querySelector('footer, #footer, .footer');
                        if(footer) footer.style.display='none';
                        var sidebar = document.querySelector('.sidebar, #sidebar, aside');
                        if(sidebar) sidebar.style.display='none';
                    })()""")
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    binding.progressBar.progress = newProgress
                }
            }

            loadUrl(AppConstants.STUDY_TIMER_URL)
        }
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) binding.webView.goBack()
        else super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() { super.onPause(); binding.webView.onPause() }
    override fun onResume() { super.onResume(); binding.webView.onResume() }

    override fun onDestroy() {
        binding.webView.apply { loadUrl("about:blank"); removeAllViews(); destroy() }
        super.onDestroy()
    }
}
