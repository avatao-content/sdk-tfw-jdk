package com.avatao.tfw.sdk.message

/**
 * A [TFWMessageScope] is used by the TFW server for routing and it specifies the direction of messages.
 *
 * It has three valid values: "zmq", "websocket", "broadcast". Their names are quite descriptive, the first
 * one forwards the message to the event handlers, the second one addresses the frontend,
 * and the third one broadcasts the message to both directions.
 */
enum class TFWMessageScope(
    val value: String
) {
    ZMQ("zmq"),
    WEBSOCKET("websocket"),
    BROADCAST("broadcast")
}