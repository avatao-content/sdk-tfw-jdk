package com.avatao.tfw.sdk.message

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Represents a message that can be sent to **TFW** using a [Connector].
 * [key] is a mandatory field that can be used as topics for message routing.
 *
 * [intent] is used to control the behavior of event listeners. Its default
 * value is [TFWMessageIntent.CONTROL].
 *
 * [scope] controls how a message is routed. Default value is [TFWMessageScope.ZMQ].
 *
 * All other fields are arbitrary and will be serialized when the message is sent.
 *
 * A [TFWMessage] implementation should contain only primitive fields and arrays/lists.
 */
interface TFWMessage {

    /**
     * Specifies the topic of a [TFWMessage]. A [key] has no default value
     * and it is mandatory in all [TFWMessage]s. It usually indicates the domain
     * of the message along with a command like `message.send`.
     *
     *  You can define your own keys and send messages with them to the TFW server,
     *  but nothing will happen until you create event handlers listening to those keys.
     *
     *  Event handlers use prefix matching on the value of the key field to determine
     *  if they should receive a given message (i.e. an event handler subscribed to `ide`
     *  will receive both `ide.write` and `ide.read` messages).
     */
    val key: String

    val intent: TFWMessageIntent
        get() = TFWMessageIntent.CONTROL

    val scope: TFWMessageScope
        get() = TFWMessageScope.ZMQ

    /**
     * Contains the raw data of this [TFWMessage] including [key], [intent] and [scope] as well.
     */
    val data: Map<String, Any>

    /**
     * Contains the raw json content of this [TFWMessage].
     */
    val rawJson: String
        get() = objectMapper.writeValueAsString(data)

    companion object {

        private val objectMapper = ObjectMapper()

        @JvmStatic
        fun builder(): TFWMessageBuilder = TFWMessageBuilder()

        @JvmStatic
        fun fromMap(map: Map<String, Any>): TFWMessage {
            val key = map["key"]?.toString() ?: error("key is missing from map")
            val scope = map["scope"]
                ?.toString()?.let { TFWMessageScope.valueOf(it.toUpperCase()) } ?: TFWMessageScope.ZMQ
            val intent = map["intent"]
                ?.toString()?.let { TFWMessageIntent.valueOf(it.toUpperCase()) } ?: TFWMessageIntent.CONTROL
            return builder()
                .withKey(key)
                .withScope(scope)
                .withIntent(intent)
                .withValues(map)
                .build()
        }
    }
}
