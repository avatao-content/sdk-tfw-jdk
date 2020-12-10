package com.avatao.tfw.sdk.api.data

import kotlinx.serialization.Serializable

/**
 * Contains the source, destination, trigger keyword and timestamp of the latest transition.
 */
@Serializable
data class LastEvent(
    val fromState: String,
    val toState: String,
    val trigger: String,
    val timestamp: String
)
