package com.avatao.tfw.sdk.api.command

import com.avatao.tfw.sdk.connector.TFWServerConnector

class WriteFile(
    private val connector: TFWServerConnector,
    data: Map<String, Any>
) {
    val filename: String by data
    val content: String by data

}