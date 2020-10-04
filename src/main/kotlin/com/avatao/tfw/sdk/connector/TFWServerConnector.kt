package com.avatao.tfw.sdk.connector

import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.message.TFWMessage
import java.io.Closeable

/**
 * A [TFWServerConnector] can be used to connect to a TFW server
 * and either [send] [TFWMessage]s or [subscribe] to them.
 */
interface TFWServerConnector : Closeable {

    fun send(message: TFWMessage)

    /**
     * Subscribes to all [TFWMessage]s with the given [key].
     * Whenever such message arrives [fn] will be called.
     */
    fun subscribe(key: String, fn: (TFWMessage) -> SubscriptionCommand): Subscription

    companion object {

        fun create(): TFWServerConnector = ZMQServerConnector()
    }
}