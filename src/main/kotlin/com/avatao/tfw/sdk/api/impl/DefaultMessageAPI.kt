package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.MessageAPI
import com.avatao.tfw.sdk.api.builder.QueueMessageBuilder
import com.avatao.tfw.sdk.api.builder.SendMessageBuilder
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription
import com.avatao.tfw.sdk.connector.TFWServerConnector

class DefaultMessageAPI(
    private val connector: TFWServerConnector
) : MessageAPI {

    override fun sendMessage() = SendMessageBuilder(connector, this)

    override fun queueMessages() = QueueMessageBuilder(connector, this)

    override fun onButtonClickHandler(fn: (command: String) -> SubscriptionCommand): Subscription {
        return connector.subscribe(EventKey.BUTTON_CLICK.value) {
            fn(it.data["value"]?.toString() ?: error("no value"))
        }
    }
}