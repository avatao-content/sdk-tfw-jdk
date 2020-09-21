/*
 * Copyright (C) 2020 Avatao.com Innovative Learning Kft.
 * All Rights Reserved. See LICENSE file for details.
 */
package sdk.util

import com.fasterxml.jackson.databind.node.ObjectNode
import org.zeromq.ZMQ
import java.nio.charset.StandardCharsets
import java.util.ArrayList

class TFWServerConnector {
    val context: ZMQ.Context = ZMQ.context(1)
    private val pushSocket: ZMQ.Socket = context.socket(ZMQ.PUSH)
    private val pushSocketConnectionString: String = "tcp://localhost:8765"// + System.getenv("TFW_PULL_PORT") // 8765

    init {
        connect()
    }

    /** Connect to TFWServer. */
    private fun connect() {
        pushSocket.connect(pushSocketConnectionString)
    }

    /** Close the pushSocket */
    fun closeSocket() {
        context.term()
    }

    /** Close connection with TFWServer. */
    fun closeContext() {
        context.term()
    }

    /**
     * Send a message to the TFW server.
     * @param message JSON message you want to send
     */
    fun send(message: ObjectNode) {
        /* Send Multipart request. */
        val messageParts = serializeTFWMessage(message)
        for (i in 0 until messageParts.size - 1) {
            pushSocket.sendMore(messageParts[i])
        }
        pushSocket.send(messageParts[messageParts.size - 1])
    }

    private fun serializeTFWMessage(message: ObjectNode): List<String> {
        /* Encode key in UTF-8. */
        val rawKey = message["key"].asText()
        val key = encodeToUtf8(rawKey)
        val value = message.toString()

        /* Add message parts to the list. */
        val messageParts: MutableList<String> = ArrayList()
        messageParts.add(key)
        messageParts.add(value)
        return messageParts
    }

    /**
     * Encodes a String to a UTF-8 encoded String.
     * @param input String to be encoded
     * @return UTF-8 encoded String
     */
    private fun encodeToUtf8(input: String): String {
        return String(input.toByteArray(), StandardCharsets.UTF_8)
    }
}