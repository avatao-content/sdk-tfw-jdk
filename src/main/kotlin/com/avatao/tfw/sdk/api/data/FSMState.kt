package com.avatao.tfw.sdk.api.data

import kotlinx.serialization.Serializable

@Serializable
data class FSMState(
    val currentState: String,
    val inAcceptedState: Boolean,
    val lastEvent: StateTransition,
    val validTransitions: List<ValidTransition>
)
