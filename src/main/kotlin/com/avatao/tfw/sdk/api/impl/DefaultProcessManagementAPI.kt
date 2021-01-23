package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.ProcessManagementAPI
import com.avatao.tfw.sdk.api.builder.ProcessLogConfigurationBuilder
import com.avatao.tfw.sdk.api.data.*
import com.avatao.tfw.sdk.connector.TFWServerConnector
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import reactor.core.publisher.Flux

class DefaultProcessManagementAPI(
    private val connector: TFWServerConnector
) : ProcessManagementAPI {

    override fun startProcess(name: String) {
        Runtime.getRuntime().exec("supervisorctl start $name")
    }

    override fun stopProcess(name: String) {
        Runtime.getRuntime().exec("supervisorctl stop $name")
    }

    override fun restartProcess(name: String){
        Runtime.getRuntime().exec("supervisorctl restart $name")
    }

    override fun readLog(): Flux<ProcessLogEntry> = Flux.create { sink ->
        connector.subscribe(EventKey.PROCESS_LOG_NEW.value) { msg ->
            sink.next(Json { ignoreUnknownKeys = true }.decodeFromString<ProcessLogEntry>(msg.rawJson))
            if (sink.isCancelled) {
                CancelSubscription
            } else {
                KeepSubscription
            }
        }
    }

    override fun configLog() = ProcessLogConfigurationBuilder(connector, this)

}
