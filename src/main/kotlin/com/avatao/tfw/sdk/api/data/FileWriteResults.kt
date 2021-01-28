package com.avatao.tfw.sdk.api.data

import kotlinx.serialization.Serializable

@Serializable
data class FileWriteResults(
    /**
     * Full path or basename of a file.
     */
    val filename: String,
    /**
     * List of every readable file according to the current patterns.
     */
    val files: List<String>
)