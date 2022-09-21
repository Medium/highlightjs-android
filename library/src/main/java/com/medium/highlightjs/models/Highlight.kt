package com.medium.highlightjs.models

data class Highlight(
    val startOffset: Int,
    val endOffset: Int,
    val text: String,
    val onClick: (() -> Unit)
)