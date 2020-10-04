package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.connector.TFWServerConnector
import java.io.Closeable

interface TFWFacade
    : Closeable, FrontendAPI, ChatBotAPI, WebIDEAPI, ConsoleAPI, TerminalAPI, ProcessManagementAPI {

    val connector: TFWServerConnector

    fun connector(fn: TFWServerConnector.() -> Unit): TFWFacade

    fun frontend(fn: FrontendAPI.() -> Unit): TFWFacade

    fun webIDE(fn: WebIDEAPI.() -> Unit): TFWFacade

    fun chatBot(fn: ChatBotAPI.() -> Unit): TFWFacade

    fun console(fn: ConsoleAPI.() -> Unit): TFWFacade

    fun terminal(fn: TerminalAPI.() -> Unit): TFWFacade

    fun process(fn: ProcessManagementAPI.() -> Unit): TFWFacade

}