package com.medium.highlightjs.utils

import com.medium.highlightjs.models.ColorSet
import com.medium.highlightjs.models.Highlight
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * This Class was created by Patrick J
 * on 09.06.16. For more Details and Licensing
 * have a look at the README.md
 */
object SourceUtils {
    fun generateContent(
        source: String,
        style: String,
        language: String?,
        supportZoom: Boolean,
        showLineNumbers: Boolean,
        isInDarkMode: Boolean,
        highlights: List<Highlight>,
        colorSet: ColorSet,
        editMode: Boolean
    ): String {
        return getStylePageHeader(supportZoom, colorSet, isInDarkMode) +
                getSourceForStyle(style) +
                (if (showLineNumbers) lineNumberStyling else "") +
                getScriptPageHeader(showLineNumbers, highlights = highlights) +
                getSourceForLanguage(source, language, editMode) +
                templateFooter
    }

    private fun getStylePageHeader(enableZoom: Boolean, colorSet: ColorSet, isInDarkMode: Boolean): String {
        return """<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
${if (enableZoom) "" else "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0\">\n"}    <style type="text/css">
       html, body {
           margin: 0px;
           padding: 0px;
           overflow-y: hidden;
       }
       pre {
           margin: 0;
       }
       
#gl-highlights-container {
    height: 100%;
    position: relative;
    user-select: none;
    touch-action: none;
    pointer-events: none;
}

.gl-highlight-overlay {
    position: absolute;
    border-radius: 2px;
    z-index: 3;
}

.gl-highlight-overlay-mine {
    background-color: ${colorSet.mine};
    ${if (!isInDarkMode) "mix-blend-mode: darken;" else "mix-blend-mode: lighten;"}
}

.gl-highlight-overlay-other {
    background-color: ${colorSet.others};
    ${if (!isInDarkMode) "mix-blend-mode: darken;" else "mix-blend-mode: lighten;"}
}
   </style>
"""
    }

    private fun getScriptPageHeader(showLineNumbers: Boolean, highlights: List<Highlight>): String {
        return """    <script src="./highlight.pack.js"></script>
            ${if (showLineNumbers) "<script src=\"./highlightjs-line-numbers.min.js\"></script>\n" else ""}    <script>hljs.highlightAll();</script>
	        <script src="./selection.js"></script>
            <script>selection.setup();</script>
           	<script src="./highlight.js"></script>
           	<script>
                const debounce = (func, delay) => {
                    let debounceTimer
                    return function() {
                        const context = this
                        const args = arguments
                            clearTimeout(debounceTimer)
                                debounceTimer
                            = setTimeout(() => func.apply(context, args), delay)
                        }
                }
           
                function onLoaded() {
                    setupHighlights(document.getElementById('gl-highlights-container'));
                    applyHighlights(${Json.encodeToString(highlights)});
                    var codeElement = document.getElementsByTagName("code")[0];
                    codeElement.addEventListener('input', debounce(function(event) {
                        var oldRange = window.getSelection().getRangeAt(0).cloneRange();
                        var clientRect = oldRange.getBoundingClientRect();
                        codeElement.classList.forEach( function(element){if(element.startsWith("language")) {codeElement.classList.remove(element);} });
                        hljs.initHighlighting.called = false;
                        hljs.highlightAll();
                        document.getSelection().removeAllRanges();
                        document.getSelection().addRange(document.caretRangeFromPoint(clientRect.x, clientRect.y + 1 ));
                        
                        var language = hljs.getLanguage(codeElement.classList[1].replace("language-", ""));
                        var languageOut ="";
                        if(language.aliases != undefined){
                            languageOut = language.aliases[0];
                        } else {
                            languageOut = language.name;
                        }
                        
                        jsBridge.onTextChange(codeElement.innerText, languageOut);
                    }, 250));
                }
            </script>
            ${if (showLineNumbers) "<script>hljs.initLineNumbersOnLoad();</script>\n" else ""}</head>
            <body style="margin: 0; padding: 0" class="hljs" onload="onLoaded()">
        """
    }

    private val lineNumberStyling: String
        private get() = """<style type="text/css">
.hljs-line-numbers {
	text-align: right;
	border-right: 1px solid #ccc;
	color: #999;
	-webkit-touch-callout: none;
	-webkit-user-select: none;
	-khtml-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}
</style>
"""
    private val templateFooter: String
        private get() = "" +
                "<div id=\"gl-highlights-container\"></div>" +
                "</body>\n</html>\n"

    private fun formatCode(code: String): String {
        return code.replace("<".toRegex(), "&lt;").replace(">".toRegex(), "&gt;")
    }

    private fun getSourceForStyle(style: String): String {
        return String.format("<link rel=\"stylesheet\" href=\"./styles/%s.css\">\n", style)
    }

    private fun getSourceForLanguage(source: String, language: String?, editMode: Boolean): String {
        return if (language != null) {
            String.format(
                "<pre><code ${if (editMode)"contentEditable=\"true\"" else ""} class=\"%s\">%s</code></pre>\n",
                language,
                formatCode(source)
            )
        } else {
            String.format("<pre><code ${if (editMode)"contentEditable=\"true\"" else ""}>%s</code></pre>\n", formatCode(source))
        }
    }
}