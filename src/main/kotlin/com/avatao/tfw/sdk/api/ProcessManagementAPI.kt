package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.builder.ProcessLogConfigurationBuilder
import com.avatao.tfw.sdk.api.data.ProcessLogEntry
import com.avatao.tfw.sdk.api.data.ProcessRestartResult
import com.avatao.tfw.sdk.api.data.ProcessStartResult
import com.avatao.tfw.sdk.api.data.ProcessStopResult
import reactor.core.publisher.Flux
import java.util.concurrent.Future

interface ProcessManagementAPI {

    /**
     *  Instructs the supervisor daemon to start a registered process.
     */
    fun startProcess(name: String): Future<ProcessStartResult>

    /**
     *  Instructs the supervisor daemon to stop a registered process.
     */
    fun stopProcess(name: String): Future<ProcessStopResult>

    /**
     *  Instructs the supervisor daemon to restart a registered process.
     */
    fun restartProcess(name: String): Future<ProcessRestartResult>

    /**
     * Returns a [Flux] of log events.
     */
    fun readLog(): Flux<ProcessLogEntry>

    /**
     * Configures the behavior of process logging events.
     */
    fun configLog(): ProcessLogConfigurationBuilder


}