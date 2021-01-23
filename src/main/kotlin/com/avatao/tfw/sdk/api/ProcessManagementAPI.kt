package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.builder.ProcessLogConfigurationBuilder
import com.avatao.tfw.sdk.api.data.ProcessLogEntry
import reactor.core.publisher.Flux

interface ProcessManagementAPI {

    /**
     *  Instructs the supervisor daemon to start a registered process.
     */
    fun startProcess(name: String)

    /**
     *  Instructs the supervisor daemon to stop a registered process.
     */
    fun stopProcess(name: String)

    /**
     *  Instructs the supervisor daemon to restart a registered process.
     */
    fun restartProcess(name: String)

    /**
     * Returns a [Flux] of log events.
     */
    fun readLog(): Flux<ProcessLogEntry>

    /**
     * Configures the behavior of process logging events.
     */
    fun configLog(): ProcessLogConfigurationBuilder
}