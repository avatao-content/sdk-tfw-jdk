package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription

interface TerminalAPI {

    /**
     *  Writes in the pseudo terminal that is connected with the frontend.
     *  It is really useful when you need to pre-type a command for the user.
     */
    fun writeToTerminal(command: String)

    /**
     * Can be used to catch commands entered by the user in the bash shell.
     */
    fun onTerminalCommand(fn: (command: String) -> SubscriptionCommand): Subscription

}