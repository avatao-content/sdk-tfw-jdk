package com.avatao.tfw.sdk.strategy

import com.avatao.tfw.sdk.api.*
import com.avatao.tfw.sdk.api.builder.*
import com.avatao.tfw.sdk.api.command.ReadFile
import com.avatao.tfw.sdk.api.command.WriteFile
import com.avatao.tfw.sdk.api.data.*
import com.avatao.tfw.sdk.connector.Subscription
import com.avatao.tfw.sdk.connector.TFWServerConnector
import reactor.core.publisher.Flux
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicReference

class TFWFacadeStub : TFWFacade {

    val deployStartCallbacks = mutableListOf<() -> SubscriptionCommand>()
    val deploySuccess = AtomicReference<Boolean>()
    val deployError = AtomicReference<String>()

    override val connector: TFWServerConnector
        get() = TODO("Not yet implemented")
    override val tfwConfig: TFWConfig
        get() = TFWConfig()

    override fun connector(fn: TFWServerConnector.() -> Unit): TFWFacade {
        TODO("not implemented")
    }

    override fun frontend(fn: FrontendAPI.() -> Unit): TFWFacade {
        TODO("not implemented")
    }

    override fun webIDE(fn: WebIDEAPI.() -> Unit): TFWFacade {
        TODO("not implemented")
    }

    override fun chatBot(fn: MessageAPI.() -> Unit): TFWFacade {
        TODO("not implemented")
    }

    override fun console(fn: ConsoleAPI.() -> Unit): TFWFacade {
        TODO("not implemented")
    }

    override fun terminal(fn: TerminalAPI.() -> Unit): TFWFacade {
        TODO("not implemented")
    }

    override fun process(fn: ProcessManagementAPI.() -> Unit): TFWFacade {
        TODO("not implemented")
    }

    override fun fsm(fn: FSMAPI.() -> Unit): TFWFacade {
        TODO("not implemented")
    }

    override fun useDeployStrategy(deployStrategy: DeployStrategy) {
        TODO("not implemented")
    }

    override fun close() {
        TODO("not implemented")
    }

    override fun configureSite(): SiteConfigurationBuilder {
        TODO("not implemented")
    }

    override fun configureDashboard(): DashboardConfigurationBuilder {
        TODO("not implemented")
    }

    override fun configureIDE(): IDEConfigurationBuilder {
        TODO("not implemented")
    }

    override fun onFrontendReady(fn: (FrontendAPI) -> Unit): Subscription {
        TODO("not implemented")
    }

    override fun sendMessage(): SendMessageBuilder {
        TODO("not implemented")
    }

    override fun queueMessages(): QueueMessageBuilder {
        TODO("not implemented")
    }

    override fun readFile(filename: String, patterns: List<String>): Future<FileContents> {
        TODO("not implemented")
    }

    override fun onReadFile(fn: (ReadFile) -> SubscriptionCommand): Subscription {
        TODO("not implemented")
    }

    override fun writeFile(filename: String, content: String) {
        TODO("not implemented")
    }

    override fun onWriteFile(fn: (WriteFile) -> SubscriptionCommand): Subscription {
        TODO("not implemented")
    }

    override fun reloadIde() {
        TODO("not implemented")
    }

    override fun onDeployStart(fn: () -> SubscriptionCommand): Subscription {
        deployStartCallbacks.add(fn)
        return SubscriptionStub()
    }

    override fun signalDeploySuccess() {
        deploySuccess.set(true)
    }

    override fun signalDeployFailure(error: String) {
        deploySuccess.set(false)
        deployError.set(error)
    }

    override fun writeToConsole(content: String) {
        TODO("not implemented")
    }

    override fun writeToTerminal(command: String) {
        TODO("not implemented")
    }

    override fun onTerminalCommand(fn: (command: String) -> SubscriptionCommand): Subscription {
        TODO("not implemented")
    }

    override fun startProcess(name: String): Future<ProcessStartResult> {
        TODO("not implemented")
    }

    override fun stopProcess(name: String): Future<ProcessStopResult> {
        TODO("not implemented")
    }

    override fun restartProcess(name: String): Future<ProcessRestartResult> {
        TODO("not implemented")
    }

    override fun readLog(): Flux<ProcessLogEntry> {
        TODO("not implemented")
    }

    override fun configLog(): ProcessLogConfigurationBuilder {
        TODO("not implemented")
    }

    override fun createFSM(states: List<String>) {
        TODO("not implemented")
    }

    override fun trigger(state: String) {
        TODO("not implemented")
    }

    override fun getCurrentState(): Future<CurrentFSMState> {
        TODO("not implemented")
    }

    override fun onUpdate(fn: (CurrentFSMState) -> SubscriptionCommand): Subscription {
        TODO("not implemented")
    }
}
