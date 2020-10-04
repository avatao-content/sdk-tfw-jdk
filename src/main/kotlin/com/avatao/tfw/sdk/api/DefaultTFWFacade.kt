package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.impl.*
import com.avatao.tfw.sdk.connector.TFWServerConnector

class DefaultTFWFacade(
    override val connector: TFWServerConnector = TFWServerConnector.create(),
    private val frontendAPI: FrontendAPI = DefaultFrontendAPI(connector),
    private val chatBotAPI: ChatBotAPI = DefaultChatBotAPI(connector),
    private val webIDEAPI: WebIDEAPI = DefaultWebIDEAPI(connector),
    private val consoleAPI: ConsoleAPI = DefaultConsoleAPI(connector),
    private val terminalAPI: TerminalAPI = DefaultTerminalAPI(connector),
    private val processManagementAPI: ProcessManagementAPI = DefaultProcessManagementAPI(connector)
) : TFWFacade,
    FrontendAPI by frontendAPI,
    ChatBotAPI by chatBotAPI,
    WebIDEAPI by webIDEAPI,
    ConsoleAPI by consoleAPI,
    TerminalAPI by terminalAPI,
    ProcessManagementAPI by processManagementAPI {

    override fun connector(fn: TFWServerConnector.() -> Unit) = also {
        fn(connector)
    }

    override fun frontend(fn: FrontendAPI.() -> Unit) = also(fn)

    override fun webIDE(fn: WebIDEAPI.() -> Unit) = also(fn)

    override fun chatBot(fn: ChatBotAPI.() -> Unit) = also(fn)

    override fun console(fn: ConsoleAPI.() -> Unit) = also(fn)

    override fun terminal(fn: TerminalAPI.() -> Unit) = also(fn)

    override fun process(fn: ProcessManagementAPI.() -> Unit) = also(fn)

    override fun close() {
        connector.close()
    }

}