package com.avatao.tfw.sdk.api.data

import kotlinx.serialization.Serializable

@Serializable
data class FileContents(
    val filename: String,
    val content: String,
    val files: List<String>
)