package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.MessageAPI
import com.avatao.tfw.sdk.api.builder.QueueMessageBuilder
import com.avatao.tfw.sdk.api.builder.SendMessageBuilder
import com.avatao.tfw.sdk.api.data.CurrentFSMState
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription
import com.avatao.tfw.sdk.connector.TFWServerConnector
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class DefaultMessageAPI(
    private val connector: TFWServerConnector
) : MessageAPI {

    override fun sendMessage() = SendMessageBuilder(connector, this)

    override fun queueMessages() = QueueMessageBuilder(connector, this)

    override fun onButtonClickHandler(fn: (button: String) -> SubscriptionCommand): Subscription {
        return connector.subscribe(EventKey.BUTTON_CLICK.value) {
            fn(Json.decodeFromString(it.rawJson))
        }
    }
}
