package com.avatao.tfw.sdk.api.data

import kotlinx.serialization.Serializable

@Serializable
data class StateTransition(
    val from_state: String,
    val to_state: String,
    val trigger: String,
    val timestamp: String
)
