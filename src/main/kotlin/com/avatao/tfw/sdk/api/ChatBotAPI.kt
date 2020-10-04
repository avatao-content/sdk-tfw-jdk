package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.builder.SendMessageBuilder

interface ChatBotAPI {

    fun sendMessage(): SendMessageBuilder

    fun queueMessages()

}