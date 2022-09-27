package com.medium.highlightjs.models

import kotlinx.serialization.Serializable

@Serializable
data class Highlight(
    val startOffset: Int,
    val endOffset: Int/*,
    val onClick: (() -> Unit)*/
)