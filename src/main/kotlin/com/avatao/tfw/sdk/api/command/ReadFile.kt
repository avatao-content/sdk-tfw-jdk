package com.avatao.tfw.sdk.api.command

import com.avatao.tfw.sdk.connector.TFWServerConnector

class ReadFile(
    private val connector: TFWServerConnector,
    data: Map<String, Any>
) {
    val filename: String by data
    val patterns: List<String> by data
}