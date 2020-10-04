package com.avatao.tfw.sdk.api.data

import kotlinx.serialization.Serializable

/**
 * Represents the result of a [EventKey.PROCESS_START] event.
 */
@Serializable
class ProcessStartResult(
    val key: String,
    /**
     * Name of of the process.
     */
    val name: String,
    /**
     * Error message from the supervisor daemon (if any).
     */
    val error: String? = null
)