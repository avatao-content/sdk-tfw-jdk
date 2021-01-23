package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.builder.QueueMessageBuilder
import com.avatao.tfw.sdk.api.builder.SendMessageBuilder
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription

interface MessageAPI {

    fun sendMessage(): SendMessageBuilder

    fun sendMessage(message: String) = sendMessage()
        .message(message)
        .originator("avataobot")
        .commit()

    fun queueMessages(): QueueMessageBuilder

    fun onButtonClickHandler(fn: (button: String) -> SubscriptionCommand): Subscription

}
