package com.avatao.tfw.sdk.api.data

import kotlinx.serialization.Serializable

@Serializable
data class StateTransition(
    val fromState: String,
    val toState: String,
    val trigger: String,
    val timestamp: String
)
