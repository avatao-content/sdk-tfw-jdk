package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.TerminalAPI
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage

class DefaultTerminalAPI(
    private val connector: TFWServerConnector
) : TerminalAPI {

    override fun writeToTerminal(command: String) {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.TERMINAL_WRITE.value)
                .withValue("command", command)
                .build()
        )
    }

    override fun onTerminalCommand(fn: (command: String) -> SubscriptionCommand): Subscription {
        return connector.subscribe(EventKey.HISTORY_BASH.value) {
            fn(it.data["command"]?.toString() ?: error("Command is missing"))
        }
    }


}