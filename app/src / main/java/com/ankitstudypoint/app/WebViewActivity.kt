package com.ankitstudypoint.app

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ankitstudypoint.app.databinding.ActivityWebviewBinding
import com.ankitstudypoint.app.utils.BookmarkManager
import com.ankitstudypoint.app.utils.NetworkUtils
import com.google.android.material.snackbar.Snackbar

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewBinding
    private lateinit var bookmarkManager: BookmarkManager
    private var currentUrl: String = ""
    private var currentTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bookmarkManager = BookmarkManager(this)

        val url = intent.getStringExtra(EXTRA_URL) ?: AppConstants.HOME_URL
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        currentUrl = url

        if (title.isNotEmpty()) {
            supportActionBar?.title = title
        }

        setupWebView()
        loadUrl(url)
    }

    private fun setupWebView() {
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = false
                displayZoomControls = false
                setSupportZoom(true)
                allowFileAccess = true
                allowContentAccess = true
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                mediaPlaybackRequiresUserGesture = false
                databaseEnabled = true
                setGeolocationEnabled(false)
                userAgentString = "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36"
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val url = request.url.toString()
                    return handleUrlOverride(url)
                }

                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBar.visibility = View.VISIBLE
                    binding.progressBar.progress = 0
                    currentUrl = url
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    binding.progressBar.visibility = View.GONE
                    currentTitle = view.title ?: ""
                    if (currentTitle.isNotEmpty()) {
                        supportActionBar?.title = currentTitle
                    }
                    injectMobileOptimizations()
                    invalidateOptionsMenu()
                }

                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    if (request.isForMainFrame) {
                        if (!NetworkUtils.isNetworkAvailable(this@WebViewActivity)) {
                            showOfflinePage()
                        }
                    }
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    binding.progressBar.progress = newProgress
                    if (newProgress == 100) {
                        binding.progressBar.visibility = View.GONE
                    }
                }

                override fun onReceivedTitle(view: WebView, title: String) {
                    currentTitle = title
                    supportActionBar?.title = title
                }
            }

            // Download listener
            setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
                handleDownload(url, userAgent, contentDisposition, mimeType)
            }
        }

        // Swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            binding.webView.reload()
            binding.swipeRefresh.isRefreshing = false
        }
        binding.swipeRefresh.setColorSchemeResources(R.color.primary, R.color.secondary)
    }

    private fun handleUrlOverride(url: String): Boolean {
        val uri = Uri.parse(url)
        val host = uri.host ?: ""

        // Open external links in browser
        val isExternal = AppConstants.EXTERNAL_DOMAINS.any { host.contains(it) }
        if (isExternal) {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
            return true
        }

        // PDFs - handle download
        if (url.endsWith(".pdf", ignoreCase = true)) {
            handleDownload(url, null, null, "application/pdf")
            return true
        }

        // Open tel: and mailto: externally
        if (url.startsWith("tel:") || url.startsWith("mailto:") || url.startsWith("whatsapp:")) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            return true
        }

        return false // Load in WebView
    }

    private fun injectMobileOptimizations() {
        val js = """
            javascript:(function(){
                // Remove ads and unnecessary elements
                var ads = document.querySelectorAll('.ad-unit, .adsbygoogle, #ad-holder');
                ads.forEach(function(el){ el.style.display='none'; });
                
                // Improve mobile readability
                var meta = document.querySelector('meta[name="viewport"]');
                if(!meta) {
                    meta = document.createElement('meta');
                    meta.name = 'viewport';
                    document.head.appendChild(meta);
                }
                meta.content = 'width=device-width, initial-scale=1.0, maximum-scale=5.0';
                
                // Smooth scroll
                document.documentElement.style.scrollBehavior = 'smooth';
                
                // Add bottom padding for bottom nav
                document.body.style.paddingBottom = '80px';
            })()
        """.trimIndent()
        binding.webView.loadUrl(js)
    }

    private fun handleDownload(url: String, userAgent: String?, contentDisposition: String?, mimeType: String?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
                return
            }
        }

        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setMimeType(mimeType)
            addRequestHeader("cookie", CookieManager.getInstance().getCookie(url))
            addRequestHeader("User-Agent", userAgent)
            setDescription("Downloading file...")
            setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                URLUtil.guessFileName(url, contentDisposition, mimeType))
        }

        val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)

        Snackbar.make(binding.root, "📥 Download started...", Snackbar.LENGTH_LONG)
            .setAction("OK") {}
            .show()
    }

    private fun showOfflinePage() {
        binding.webView.loadData(getOfflineHtml(), "text/html", "UTF-8")
    }

    private fun getOfflineHtml(): String = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <style>
                body { 
                    font-family: sans-serif; text-align: center; 
                    padding: 40px 20px; background: #f5f5f5; 
                }
                .icon { font-size: 80px; margin-bottom: 20px; }
                h2 { color: #333; }
                p { color: #666; line-height: 1.6; }
                button { 
                    background: #4CAF50; color: white; border: none;
                    padding: 12px 30px; border-radius: 25px; font-size: 16px;
                    cursor: pointer; margin-top: 20px;
                }
            </style>
        </head>
        <body>
            <div class="icon">📡</div>
            <h2>No Internet Connection</h2>
            <p>Please check your connection and try again.<br>
            Some content may be available offline.</p>
            <button onclick="window.location.reload()">🔄 Retry</button>
        </body>
        </html>
    """.trimIndent()

    private fun loadUrl(url: String) {
        if (NetworkUtils.isNetworkAvailable(this)) {
            binding.webView.loadUrl(url)
        } else {
            showOfflinePage()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.webview_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val isBookmarked = bookmarkManager.isBookmarked(currentUrl)
        menu.findItem(R.id.menu_bookmark)?.setIcon(
            if (isBookmarked) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark
        )
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_bookmark -> {
                toggleBookmark()
                true
            }
            R.id.menu_share -> {
                shareCurrentPage()
                true
            }
            R.id.menu_refresh -> {
                binding.webView.reload()
                true
            }
            R.id.menu_open_browser -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl)))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleBookmark() {
        val bookmark = BookmarkManager.Bookmark(
            url = currentUrl,
            title = currentTitle.ifEmpty { currentUrl },
            timestamp = System.currentTimeMillis()
        )

        if (bookmarkManager.isBookmarked(currentUrl)) {
            bookmarkManager.removeBookmark(currentUrl)
            Toast.makeText(this, "🗑️ Bookmark removed", Toast.LENGTH_SHORT).show()
        } else {
            bookmarkManager.addBookmark(bookmark)
            Toast.makeText(this, "⭐ Page bookmarked!", Toast.LENGTH_SHORT).show()
        }
        invalidateOptionsMenu()
    }

    private fun shareCurrentPage() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, currentTitle)
            putExtra(Intent.EXTRA_TEXT, "📚 Check this out on Ankit Study Point!\n\n$currentTitle\n$currentUrl")
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    override fun onDestroy() {
        binding.webView.apply {
            loadUrl("about:blank")
            clearHistory()
            removeAllViews()
            destroy()
        }
        super.onDestroy()
    }

    companion object {
        const val EXTRA_URL   = "extra_url"
        const val EXTRA_TITLE = "extra_title"
    }
}
