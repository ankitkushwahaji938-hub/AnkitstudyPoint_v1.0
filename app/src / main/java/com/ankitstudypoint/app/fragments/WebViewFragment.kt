package com.ankitstudypoint.app.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ankitstudypoint.app.R
import com.ankitstudypoint.app.utils.NetworkUtils

abstract class WebViewFragment : Fragment() {

    protected var webView: WebView? = null
    protected var progressBar: View? = null
    protected var swipeRefresh: SwipeRefreshLayout? = null

    abstract val pageUrl: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.web_view)
        progressBar = view.findViewById(R.id.progress_bar)
        swipeRefresh = view.findViewById(R.id.swipe_refresh)

        setupWebView()
        setupSwipeRefresh()

        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            webView?.loadUrl(pageUrl)
        } else {
            showOfflinePage()
        }
    }

    private fun setupWebView() {
        webView?.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = false
                displayZoomControls = false
                setSupportZoom(true)
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                mediaPlaybackRequiresUserGesture = false
                databaseEnabled = true
                userAgentString = "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36"
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val url = request.url.toString()
                    return onUrlLoading(url)
                }

                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    progressBar?.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView, url: String) {
                    progressBar?.visibility = View.GONE
                    swipeRefresh?.isRefreshing = false
                    injectOptimizations()
                }

                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    if (request.isForMainFrame) {
                        progressBar?.visibility = View.GONE
                        showOfflinePage()
                    }
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    if (newProgress == 100) {
                        progressBar?.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefresh?.apply {
            setOnRefreshListener {
                if (NetworkUtils.isNetworkAvailable(requireContext())) {
                    webView?.reload()
                } else {
                    isRefreshing = false
                    showOfflinePage()
                }
            }
            setColorSchemeResources(R.color.primary, R.color.secondary)
        }
    }

    open fun onUrlLoading(url: String): Boolean = false

    private fun injectOptimizations() {
        val js = """javascript:(function(){
            var meta = document.querySelector('meta[name="viewport"]');
            if(meta){ meta.content='width=device-width,initial-scale=1,maximum-scale=5'; }
            document.body.style.paddingBottom='70px';
            document.documentElement.style.scrollBehavior='smooth';
        })()"""
        webView?.loadUrl(js)
    }

    private fun showOfflinePage() {
        webView?.loadData(OFFLINE_HTML, "text/html", "UTF-8")
    }

    fun canGoBack(): Boolean = webView?.canGoBack() == true

    fun goBack() { webView?.goBack() }

    override fun onResume() {
        super.onResume()
        webView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView?.onPause()
    }

    override fun onDestroyView() {
        webView?.apply {
            loadUrl("about:blank")
            removeAllViews()
            destroy()
        }
        webView = null
        super.onDestroyView()
    }

    companion object {
        private const val OFFLINE_HTML = """
            <!DOCTYPE html><html><head>
            <meta name="viewport" content="width=device-width,initial-scale=1">
            <style>
                body{font-family:sans-serif;text-align:center;padding:60px 20px;background:#FAFAFA}
                .emoji{font-size:72px;display:block;margin-bottom:24px}
                h2{color:#212121;font-size:22px}
                p{color:#757575;line-height:1.7;font-size:15px}
                .btn{display:inline-block;background:#4CAF50;color:white;padding:14px 32px;
                     border-radius:30px;text-decoration:none;font-size:16px;margin-top:24px;border:none;cursor:pointer}
            </style></head><body>
            <span class="emoji">📡</span>
            <h2>You're Offline</h2>
            <p>No internet connection detected.<br>Please check your network and try again.</p>
            <button class="btn" onclick="window.location.reload()">🔄 Retry</button>
            </body></html>
        """
    }
}
