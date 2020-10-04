package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.ChatBotAPI
import com.avatao.tfw.sdk.api.builder.SendMessageBuilder
import com.avatao.tfw.sdk.connector.TFWServerConnector

class DefaultChatBotAPI(
    private val connector: TFWServerConnector
) : ChatBotAPI {

    override fun sendMessage() = SendMessageBuilder(connector, this)

    override fun queueMessages() {
        TODO("not clear how this is supposed to work")
    }

}