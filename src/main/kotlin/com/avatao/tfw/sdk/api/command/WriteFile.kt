package com.avatao.tfw.sdk.api.command

import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.FileWriteResults
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage

class WriteFile(
    private val connector: TFWServerConnector,
    data: Map<String, Any>
) {
    val filename: String by data
    val content: String by data

    fun respondWith(results: FileWriteResults) {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.IDE_READ.value)
                .withValue(FileWriteResults::filename.name, results.filename)
                .withValue(FileWriteResults::files.name, results.files)
                .build()
        )
    }
}