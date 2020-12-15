package com.avatao.tfw.sdk.api.builder

import com.avatao.tfw.sdk.api.MessageAPI
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.impl.tryWithValue
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage

/**
 *  Sends a message to the chat bot.
 */
class QueueMessageBuilder(
    private val connector: TFWServerConnector,
    private val messageAPI: MessageAPI,
    private val messages: MutableList<SendMessageBuilder> = mutableListOf(),
) {

    fun message(message: SendMessageBuilder) = also {
        messages.add(message)
    }

    @JvmSynthetic
    fun message(fn: SendMessageBuilder.() -> Unit) = also {
        val smb = SendMessageBuilder(connector, messageAPI)
        fn(smb)
        messages.add(smb)
    }

    fun build() = TFWMessage.builder()
        .withKey(EventKey.MESSAGE_QUEUE.value)
        .tryWithValue(messagesKey, messages.map { it.build().data.minus("key") })
        .build()

    /**
     * Builds and sends the messages to TFW.
     */
    fun commit(): MessageAPI {
        require(messages.isNotEmpty()) {
            "There must be at least one message."
        }
        connector.send(build())
        return messageAPI
    }

    companion object {
        private const val messagesKey = "messages"
    }
}
