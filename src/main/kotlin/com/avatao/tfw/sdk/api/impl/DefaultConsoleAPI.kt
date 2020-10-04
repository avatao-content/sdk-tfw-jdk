package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.ConsoleAPI
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage

class DefaultConsoleAPI(
    private val connector: TFWServerConnector
) : ConsoleAPI {


    override fun readConsole(): String {
        TODO("Not clear how this works yet")
    }

    override fun writeToConsole(content: String) {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.CONSOLE_WRITE.value)
                .withValue("content", content)
                .build()
        )
    }


}