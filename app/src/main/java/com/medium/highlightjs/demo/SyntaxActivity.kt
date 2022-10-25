package com.medium.highlightjs.demo

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.medium.highlightjs.HighlightJsView
import com.medium.highlightjs.HighlightJsView.OnLanguageChangedListener
import com.medium.highlightjs.HighlightJsView.OnThemeChangedListener
import com.medium.highlightjs.demo.utils.ThemeChangerDialog
import com.medium.highlightjs.demo.utils.ThemeChangerDialog.ThemeChangeListener
import com.medium.highlightjs.models.Highlight
import com.medium.highlightjs.models.Language
import com.medium.highlightjs.models.SelectionCallback
import com.medium.highlightjs.models.Theme
import timber.log.Timber
import java.util.*

class SyntaxActivity : AppCompatActivity(), OnRefreshListener, OnThemeChangedListener, ThemeChangeListener,
    OnLanguageChangedListener {
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var highlightJsView: HighlightJsView? = null
    private var themeChangerDialog: ThemeChangerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_syntax)
        //        FileObject fileObject = (FileObject) getIntent().getExtras().getSerializable("fileObject");
        if (actionBar != null) {
/*            assert fileObject != null;
            getActionBar().setTitle(fileObject.getFileName());*/
            actionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            /*        assert fileObject != null;
            getSupportActionBar().setTitle(fileObject.getFileName());*/
        }
        //set and assign swipe refresh listener
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout?.setOnRefreshListener(this)
        //find and instantiate the view
        highlightJsView = findViewById(R.id.highlight_view)
        //register theme change listener
        highlightJsView?.setOnThemeChangedListener(this)
        //change theme and set language to auto detect
        highlightJsView?.setTheme(Theme.MEDIUM_LIGHT)
        //assert fileObject != null;
        //String[] name = fileObject.getFileName().split("\\.");
        highlightJsView?.setOnLanguageChangedListener(this)
        /*
        if (name.length > 0)
            highlightJsView.setLanguageByFileExtension(name[name.length - 1]);*/
        //load the source
        val source = """public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main, FilesListFragment.newInstance())
                .commit();
    }
}
"""
        highlightJsView?.highlightLanguage = Language.JAVA
        highlightJsView?.setSource(
            source, listOf(
                Highlight(
                    startOffset = 1,
                    endOffset = 12,
                    false
                ),
                Highlight(
                    startOffset = 15,
                    endOffset = 40,
                    true
                ),
                Highlight(
                    startOffset = 78,
                    endOffset = 200,
                    false
                ),
            ), listOf(
                {
                    Log.e("Alex", "0")
                },
                {
                    Log.e("Alex", "1")
                },
                {
                    Log.e("Alex", "2")
                },

                )
        )
        highlightJsView?.selectionCallback = object : SelectionCallback {
            override fun onSelectionChange(selectedText: String?) {
                Log.d("SyntaxActivity:onSelectionChange", selectedText.orEmpty())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        themeChangerDialog = ThemeChangerDialog(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(this).inflate(R.menu.menu_theme_switch, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.menu_switch_theme -> themeChangerDialog!!.show(this)
            R.id.menu_check_line_numbers -> {
                item.isChecked = !item.isChecked
                onShowLineNumbersToggled(item.isChecked)
            }
            R.id.menu_check_zoom -> {
                item.isChecked = !item.isChecked
                onZoomSupportToggled(item.isChecked)
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onShowLineNumbersToggled(enableLineNumbers: Boolean) {
        highlightJsView!!.setShowLineNumbers(enableLineNumbers)
        highlightJsView!!.refresh()
    }

    private fun onZoomSupportToggled(enableZooming: Boolean) {
        highlightJsView!!.setZoomSupportEnabled(enableZooming)
        highlightJsView!!.refresh()
    }

    override fun onRefresh() {
        swipeRefreshLayout!!.isRefreshing = true
        highlightJsView!!.setTheme(getRandom(*Theme.values()))
        highlightJsView!!.refresh()
    }

    private fun <T> getRandom(vararg items: T): T {
        return items[Random().nextInt(items.size)]
    }

    override fun onThemeChanged(theme: Theme) {
        swipeRefreshLayout!!.isRefreshing = false
        Toast.makeText(this, "Theme: " + theme.name, Toast.LENGTH_SHORT).show()
    }

    override fun onChangeTheme(theme: Theme) {
        highlightJsView!!.setTheme(theme)
        highlightJsView!!.refresh()
    }

    override fun onLanguageChanged(language: Language) {
        Toast.makeText(this, language.languageName, Toast.LENGTH_LONG).show()
    }
}