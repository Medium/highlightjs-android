package com.medium.highlightjs

import com.medium.highlightjs.utils.ExtensionUtil.getLanguageByExtension
import com.medium.highlightjs.utils.FileUtils.loadSourceFromFile
import com.medium.highlightjs.utils.FileUtils.loadSourceFromUrl
import android.webkit.WebView
import android.annotation.TargetApi
import android.os.Build
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import com.medium.highlightjs.models.*
import com.medium.highlightjs.utils.FileUtils
import com.medium.highlightjs.utils.SourceUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.net.URL

/**
 * This Class was created by Patrick J
 * on 09.06.16. For more Details and Licensing
 * have a look at the README.md
 */
class HighlightJsView : WebView, FileUtils.Callback {
    //local variables to store language and theme
    private var language = Language.AUTO_DETECT
    private var theme = Theme.DEFAULT
    private var content: String? = null
    private var highlights: List<Highlight> = emptyList()
    private var highlightListener: List<(() -> Unit)> = emptyList()
    private var zoomSupport = true
    private var showLineNumbers = false

    //local variables to register callbacks
    private var onLanguageChangedListener: OnLanguageChangedListener? = null
    private var onThemeChangedListener: OnThemeChangedListener? = null
    private var onContentChangedListener: OnContentChangedListener? = null

    var colorSet: ColorSet = ColorSet(mine = "#85F8CA", others = "#E5FDF3")
    var selectionCallback: SelectionCallback? = null
    var editMode: Boolean = false
    var latestText: String = ""

    override fun onDataLoaded(success: Boolean, source: String?) {
        if (success) setSource(source)
    }

    interface OnLanguageChangedListener {
        fun onLanguageChanged(language: Language)
    }

    interface OnThemeChangedListener {
        fun onThemeChanged(theme: Theme)
    }

    interface OnContentChangedListener {
        fun onContentChanged()
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initView(context)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView(context: Context) {
        //make sure the view is blank
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        };
        loadUrl("about:blank")
        //set the settings for the view
        //WebSettings settings = getSettings();
        //settings.setJavaScriptEnabled(true);
        //settings.setBuiltInZoomControls(true);
        //settings.setSupportZoom(zoomSupport);
        //disable zoom controls on +Honeycomb devices

        //to remove padding and margin
        scrollBarStyle = SCROLLBARS_INSIDE_OVERLAY
        isScrollbarFadingEnabled = true
        val settings = settings
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = false
        settings.useWideViewPort = false
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setLayerType(LAYER_TYPE_HARDWARE, null)
        } else {
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
        settings.displayZoomControls = false
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        }
        isHorizontalScrollBarEnabled = false

        this.addJavascriptInterface(JsInterface(), "jsBridge")
    }

    private fun changeZoomSettings(enable: Boolean) {
        zoomSupport = enable
        settings.setSupportZoom(enable)
    }

    /**
     * Attach a callback to receive calls when the Highlight-Language has changed
     * @param onLanguageChangedListener
     */
    fun setOnLanguageChangedListener(onLanguageChangedListener: OnLanguageChangedListener?) {
        this.onLanguageChangedListener = onLanguageChangedListener
    }

    /**
     * Attach a callback to receive calls when the Theme has changed
     * @param onThemeChangedListener
     */
    fun setOnThemeChangedListener(onThemeChangedListener: OnThemeChangedListener?) {
        this.onThemeChangedListener = onThemeChangedListener
    }

    /**
     * Attach a callback to receive calls when the content has changed
     * @param onContentChangedListener
     */
    fun setOnContentChangedListener(onContentChangedListener: OnContentChangedListener?) {
        this.onContentChangedListener = onContentChangedListener
    }

    /**
     * Set the desired language to highlight the given source by the extension of file which contained source.
     * If It can't find extension type it'd set language to [Language.AUTO_DETECT].
     * Default: [Language.AUTO_DETECT]
     * @param extension - Extension of file with contains code.
     */
    fun setLanguageByFileExtension(extension: String) {
        highlightLanguage = getLanguageByExtension(extension.toLowerCase())
    }

    /**
     * Set the desired theme to highlight the given source.
     * Default: [Theme.DEFAULT]
     * @param theme
     */
    fun setTheme(theme: Theme) {
        this.theme = theme
        //notify the callback (if set)
        if (onThemeChangedListener != null) onThemeChangedListener!!.onThemeChanged(theme)
    }
    /**
     * Receive the [Language] which is currently highlighted.
     * @return The [Language] which is currently highlighted.
     *///notify the callback (if set)
    /**
     * Set the desired language to highlight the given source.
     * Default: [Language.AUTO_DETECT]
     * @param language
     */
    var highlightLanguage: Language
        get() = language
        set(language) {
            this.language = language
            //notify the callback (if set)
            if (onLanguageChangedListener != null) onLanguageChangedListener!!.onLanguageChanged(language)
        }

    /**
     * Receive the [Theme] which is currently set.
     * @return The [Theme] which is currently set.
     */
    fun getTheme(): Theme {
        return theme
    }

    /**
     * Set the source to highlight from a String
     * @param source - The source as [String]
     */
    fun setSource(source: String?) {
        setSource(source, emptyList(), emptyList())
    }

    fun setSource(
        source: String?,
        list: List<Highlight>,
        listeners: List<(() -> Unit)>
    ) {
        if (source != null && source.length != 0) {
            //generate and load the content
            content = source
            highlights = list
            highlightListener = listeners
            val isInDarkMode = context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            val page = SourceUtils.generateContent(
                source,
                theme.getName(),
                language.getName(),
                zoomSupport,
                showLineNumbers,
                isInDarkMode,
                list,
                colorSet,
                editMode
            )
            val start = System.currentTimeMillis()
            try {
                loadDataWithBaseURL("file:///android_asset/", page, "text/html", "utf-8", null)
            } finally {
                println("로딩 : " + (System.currentTimeMillis() - start))
            }
            //notify the callback (if set)
            if (onContentChangedListener != null) onContentChangedListener!!.onContentChanged()
        } else Log.e(javaClass.simpleName, "Source can't be null or empty.")
    }

    /**
     * Set the source to highlight from a File
     * @param source - The source as [File]
     */
    fun setSource(source: File) {
        //try to encode and set the source
        val encSource = loadSourceFromFile(source)
        encSource?.let { setSource(it) } ?: Log.e(javaClass.simpleName, "Unable to encode file: " + source.absolutePath)
    }

    /**
     * Set the source to highlight from a remote URL
     * @param url - The source as [URL]
     */
    fun setSource(url: URL?) {
        //try to encode and set the source
        loadSourceFromUrl(this, url!!)
    }

    /**
     * Refresh the View.
     * Needs to be called when setting a new language, theme or source.
     */
    fun refresh() {
        if (content != null) {
            loadUrl("about:blank")
            setSource(content)
        }
    }

    /**
     * Enable or disable zooming functionality.
     * Zooming is disabled by default.
     * @param supportZoom - true to enable, false to disable zoom
     */
    fun setZoomSupportEnabled(supportZoom: Boolean) {
        changeZoomSettings(supportZoom)
    }

    /**
     * Enable or disable line numbers on the left side of the code snippet.
     * Line numbers are disabled by default
     * @param showLineNumbers - true to show - false to hide line numbers next to the code
     */
    fun setShowLineNumbers(showLineNumbers: Boolean) {
        this.showLineNumbers = showLineNumbers
    }

    inner class JsInterface() {
        @JavascriptInterface
        fun onSelectionChange(value: String?) {
            selectionCallback?.onSelectionChange(value)
        }

        @JavascriptInterface
        fun onHighlightClick(index: Int) {
            highlightListener[index]()
        }

        @JavascriptInterface
        fun onTextChange(text: String) {
            latestText = text
        }
    }
}