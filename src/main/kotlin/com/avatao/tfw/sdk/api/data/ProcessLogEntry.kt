package com.avatao.tfw.sdk.api.data

import kotlinx.serialization.Serializable

@Serializable
data class ProcessLogEntry(
    val stdout: String,
    val stderr: String
)
