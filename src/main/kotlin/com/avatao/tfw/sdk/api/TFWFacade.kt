package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.api.data.TFWConfig
import com.avatao.tfw.sdk.connector.Subscription
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage
import com.avatao.tfw.sdk.strategy.DeployStrategy
import java.io.Closeable

interface TFWFacade
    : Closeable, FrontendAPI, MessageAPI, WebIDEAPI, ConsoleAPI, TerminalAPI, ProcessManagementAPI, FSMAPI {

    val connector: TFWServerConnector
    val tfwConfig: TFWConfig

    fun send(message: TFWMessage) = connector.send(message)

    fun subscribe(key: String, fn: (TFWMessage) -> SubscriptionCommand): Subscription {
        return connector.subscribe(key, fn)
    }

    fun connector(fn: TFWServerConnector.() -> Unit): TFWFacade

    fun frontend(fn: FrontendAPI.() -> Unit): TFWFacade

    fun webIDE(fn: WebIDEAPI.() -> Unit): TFWFacade

    fun chatBot(fn: MessageAPI.() -> Unit): TFWFacade

    fun console(fn: ConsoleAPI.() -> Unit): TFWFacade

    fun terminal(fn: TerminalAPI.() -> Unit): TFWFacade

    fun process(fn: ProcessManagementAPI.() -> Unit): TFWFacade

    fun fsm(fn: FSMAPI.() -> Unit): TFWFacade

    /**
     * Configures the given [DeployStrategy] to be used with this [TFWFacade].
     */
    fun useDeployStrategy(deployStrategy: DeployStrategy)

}
