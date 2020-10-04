package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.ProcessManagementAPI
import com.avatao.tfw.sdk.api.builder.ProcessLogConfigurationBuilder
import com.avatao.tfw.sdk.api.data.*
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import reactor.core.publisher.Flux
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class DefaultProcessManagementAPI(
    private val connector: TFWServerConnector
) : ProcessManagementAPI {

    override fun startProcess(name: String): Future<ProcessStartResult> {
        val result = CompletableFuture<ProcessStartResult>()
        connector.subscribe(EventKey.PROCESS_START.value) {
            val psr = Json.decodeFromString<ProcessStartResult>(it.rawJson)
            if (psr.name == name) {
                result.complete(psr)
                CancelSubscription
            } else {
                KeepSubscription
            }
        }
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.PROCESS_START.value)
                .withValue("name", name)
                .build()
        )
        return result
    }

    override fun stopProcess(name: String): Future<ProcessStopResult> {
        val result = CompletableFuture<ProcessStopResult>()
        connector.subscribe(EventKey.PROCESS_STOP.value) {
            val psr = Json.decodeFromString<ProcessStopResult>(it.rawJson)
            if (psr.name == name) {
                result.complete(psr)
                CancelSubscription
            } else {
                KeepSubscription
            }
        }
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.PROCESS_STOP.value)
                .withValue("name", name)
                .build()
        )
        return result
    }

    override fun restartProcess(name: String): Future<ProcessRestartResult> {
        val result = CompletableFuture<ProcessRestartResult>()
        connector.subscribe(EventKey.PROCESS_RESTART.value) {
            val prr = Json.decodeFromString<ProcessRestartResult>(it.rawJson)
            if (prr.name == name) {
                result.complete(prr)
                CancelSubscription
            } else {
                KeepSubscription
            }
        }
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.PROCESS_RESTART.value)
                .withValue("name", name)
                .build()
        )
        return result
    }

    override fun readLog(): Flux<ProcessLogEntry> = Flux.create { sink ->
        connector.subscribe(EventKey.PROCESS_LOG_NEW.value) { msg ->
            sink.next(Json.decodeFromString<ProcessLogEntry>(msg.rawJson))
            if (sink.isCancelled) {
                CancelSubscription
            } else {
                KeepSubscription
            }
        }
    }

    override fun configLog() = ProcessLogConfigurationBuilder(connector, this)

}