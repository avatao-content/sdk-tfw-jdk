package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.builder.QueueMessageBuilder
import com.avatao.tfw.sdk.api.builder.SendMessageBuilder

interface MessageAPI {

    fun sendMessage(): SendMessageBuilder

    fun queueMessages(): QueueMessageBuilder

}
