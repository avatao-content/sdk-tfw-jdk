package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.MessageAPI
import com.avatao.tfw.sdk.api.builder.QueueMessageBuilder
import com.avatao.tfw.sdk.api.builder.SendMessageBuilder
import com.avatao.tfw.sdk.connector.TFWServerConnector

class DefaultMessageAPI(
    private val connector: TFWServerConnector
) : MessageAPI {

    override fun sendMessage() = SendMessageBuilder(connector, this)

    override fun queueMessages() = QueueMessageBuilder(connector, this)

}
