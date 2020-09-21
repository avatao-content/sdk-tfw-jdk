/*
 * Copyright (C) 2020 Avatao.com Innovative Learning Kft.
 * All Rights Reserved. See LICENSE file for details.
 */
package sdk.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode

/**
 * Provides a mechanism to send messages to our frontend messaging component which
 * displays messages with the key "message".
 */
class MessageSender(private var serverConnector: TFWServerConnector) {

    /**
     * Sends a message to the key specified in the constructor.
     * @param message message to send
     */
    fun send(message: String?, originator: String) {
        val mapper = ObjectMapper()
        val tfwMessage = mapper.createObjectNode()
        tfwMessage.put("key", "message.send")
        tfwMessage.put("originator", originator)
        tfwMessage.put("message", message)
        serverConnector.send(tfwMessage)
    }

    /**
     * Queue a list of messages to be displayed in a chatbot-like manner.
     * @param originator name of sender to be displayed on the frontend
     * @param messages list of messages to queue
     */
    fun queueMessages(messages: List<String?>, originator: String = "avataobot") {
        val mapper = ObjectMapper()
        val messageArray = createMessagesJsonArray(messages,originator)

        /* Build message. */
        val tfwMessage = mapper.createObjectNode()
        tfwMessage.put("key", "message.queue")
        tfwMessage.put("messages", messageArray)
        serverConnector.send(tfwMessage)
    }

    /**
     * Create a JSON array out of a originator and a message queue.
     * @param originator name of sender to be displayed on the frontend
     * @param messages list of messages to queue
     */
    private fun createMessagesJsonArray(
        messages: List<String?>,
        originator: String
    ): ArrayNode {
        val mapper = ObjectMapper()
        val messageArray = mapper.createArrayNode()
        for (message in messages) {
            val messageNode = mapper.createObjectNode()
            messageNode.put("message", message)
            messageNode.put("originator", originator)
            messageArray.add(messageNode)
        }
        return messageArray
    }

}