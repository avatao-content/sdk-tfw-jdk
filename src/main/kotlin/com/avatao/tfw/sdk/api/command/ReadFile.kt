package com.avatao.tfw.sdk.api.command

import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.FileContents
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage

class ReadFile(
    private val connector: TFWServerConnector,
    data: Map<String, Any>
) {

    val filename: String by data
    val patterns: List<String> by data

    fun respondWith(fileContents: FileContents) {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.IDE_READ.value)
                .withValue(FileContents::filename.name, fileContents.filename)
                .withValue(FileContents::content.name, fileContents.content)
                .withValue(FileContents::files.name, fileContents.files)
                .build()
        )
    }

}