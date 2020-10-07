package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.data.TFWConfig
import com.avatao.tfw.sdk.api.impl.*
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.strategy.DeployStrategy

class DefaultTFWFacade(
    override val connector: TFWServerConnector = TFWServerConnector.create(),
    override val tfwConfig: TFWConfig = TFWConfig(),
    private val frontendAPI: FrontendAPI = DefaultFrontendAPI(connector),
    private val messageAPI: MessageAPI = DefaultMessageAPI(connector),
    private val webIDEAPI: WebIDEAPI = DefaultWebIDEAPI(connector),
    private val consoleAPI: ConsoleAPI = DefaultConsoleAPI(connector),
    private val terminalAPI: TerminalAPI = DefaultTerminalAPI(connector),
    private val processManagementAPI: ProcessManagementAPI = DefaultProcessManagementAPI(connector),
    private val fsmApi: FSMAPI = DefaultFSMAPI(connector)
) : TFWFacade,
    FrontendAPI by frontendAPI,
    MessageAPI by messageAPI,
    WebIDEAPI by webIDEAPI,
    ConsoleAPI by consoleAPI,
    TerminalAPI by terminalAPI,
    ProcessManagementAPI by processManagementAPI,
    FSMAPI by fsmApi {

    private val strategies = mutableListOf<DeployStrategy>()

    override fun connector(fn: TFWServerConnector.() -> Unit) = also {
        fn(connector)
    }

    override fun frontend(fn: FrontendAPI.() -> Unit) = also(fn)

    override fun webIDE(fn: WebIDEAPI.() -> Unit) = also(fn)

    override fun chatBot(fn: MessageAPI.() -> Unit) = also(fn)

    override fun console(fn: ConsoleAPI.() -> Unit) = also(fn)

    override fun terminal(fn: TerminalAPI.() -> Unit) = also(fn)

    override fun process(fn: ProcessManagementAPI.() -> Unit) = also(fn)

    override fun fsm(fn: FSMAPI.() -> Unit) = also(fn)

    override fun useDeployStrategy(deployStrategy: DeployStrategy) {
        strategies.add(deployStrategy)
        deployStrategy.configure(this)
    }

    override fun close() {
        strategies.forEach {
            try {
                it.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        connector.close()
    }

}
