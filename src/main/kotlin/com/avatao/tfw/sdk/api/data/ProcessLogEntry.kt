package com.avatao.tfw.sdk.api.data

import kotlinx.serialization.Serializable

@Serializable
class ProcessLogEntry(
    val stdout: String,
    val stderr: String
)