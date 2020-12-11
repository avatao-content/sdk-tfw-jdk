package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.builder.QueueMessageBuilder
import com.avatao.tfw.sdk.api.builder.SendMessageBuilder
import com.avatao.tfw.sdk.api.data.CurrentFSMState
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

interface MessageAPI {

    fun sendMessage(): SendMessageBuilder

    fun sendMessage(message: String) = sendMessage()
        .message(message)
        .commit()

    fun queueMessages(): QueueMessageBuilder

    fun onButtonClickHandler(fn: (button: String) -> SubscriptionCommand): Subscription

}
