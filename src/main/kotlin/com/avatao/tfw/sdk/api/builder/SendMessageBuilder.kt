package com.avatao.tfw.sdk.api.builder

import com.avatao.tfw.sdk.api.MessageAPI
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.impl.tryWithValue
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage

/**
 *  Sends a message to the chat bot.
 */
class SendMessageBuilder(
    private val connector: TFWServerConnector,
    private val messageAPI: MessageAPI,
    private var message: String? = null,
    private var originator: String = "avataobot",
    private var wpm: Int? = null,
    private var typing: Boolean? = null,
    private var command: String? = null,
    private var buttons: List<String>? = null
) {

    /**
     * Body of the message, should be Markdown compatible.
     */
    fun message(message: String) = also {
        this.message = message
    }

    /**
     * Name of the sender.
     */
    fun originator(originator: String) = also {
        this.originator = originator
    }

    /**
     * Tunes typing speed based on words per minute.
     */
    fun wpm(wpm: Int) = also {
        this.wpm = wpm
    }

    /**
     * Displays typing indicator when true.
     */
    fun typing(typing: Boolean) = also {
        this.typing = typing
    }

    /**
     * Send this message to the TFW server when the message is clicked.
     */
    fun command(command: String) = also {
        this.command = command
    }

    /**
     * Adding buttons to the message.
     * Buttons can be: 'yes', 'no', 'solution', 'hint', 'fix'
     */
    fun buttons(buttons: List<String>) = also {
        this.buttons = buttons
    }

    fun build(): TFWMessage {
        require(message != null) {
            "Message is mandatory when sending a message."
        }
        return TFWMessage.builder()
            .withKey(EventKey.MESSAGE_SEND.value)
            .tryWithValue(messageKey, message)
            .tryWithValue(originatorKey, originator)
            .tryWithValue(wpmKey, wpm)
            .tryWithValue(typingKey, typing)
            .tryWithValue(commandKey, command)
            .tryWithValue(buttonsKey, buttons)
            .build()
    }

    /**
     * Builds and sends the message to TFW.
     */
    fun commit(): MessageAPI {
        connector.send(build())
        return messageAPI
    }

    companion object {
        private const val messageKey = "message"
        private const val originatorKey = "originator"
        private const val wpmKey = "wpm"
        private const val typingKey = "typing"
        private const val commandKey = "command"
        private const val buttonsKey = "buttons"
    }
}
