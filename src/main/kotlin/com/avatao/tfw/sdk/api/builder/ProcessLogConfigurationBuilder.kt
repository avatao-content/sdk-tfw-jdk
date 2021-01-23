package com.avatao.tfw.sdk.api.builder

import com.avatao.tfw.sdk.api.ProcessManagementAPI
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.impl.tryWithValue
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage

/**
 * Configures the behavior of process logging events.
 */
class ProcessLogConfigurationBuilder(
    private val connector: TFWServerConnector,
    private val processManagementAPI: ProcessManagementAPI,
    private var name: String? = null,
    private var tail: Long? = null
) {

    /**
     * Name of the process to watch.
     */
    fun name(name: String) = also {
        this.name = name
    }

    /**
     * Limit the number of log entries to list.
     */
    fun tail(tail: Long) = also {
        this.tail = tail
    }

    /**
     * Builds and sends the configuration to TFW.
     */
    fun commit(): ProcessManagementAPI {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.PROCESS_LOG_SET.value)
                .tryWithValue(nameKey, name)
                .tryWithValue(tailKey, tail)
                .build()
        )
        return processManagementAPI
    }

    companion object {
        private const val nameKey = "name"
        private const val tailKey = "tail"
    }
}