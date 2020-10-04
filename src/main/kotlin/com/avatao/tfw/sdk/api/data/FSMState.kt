package com.avatao.tfw.sdk.api.data

import kotlinx.serialization.Serializable

@Serializable
class FSMState(
    /**
     * Name of the current FSM state.
     */
    val currentState: String,
    /**
     * Tells whether the FSM is in its final state. On the platform, the challenge is considered
     * finished when this property is true.
     */
    val inAcceptedState: Boolean,
    /**
     * Contains the source, destination, trigger keyword and timestamp of the latest transition.
     */
    val lastEvent: LastEvent,
    /**
     * Lists every possible transitions from the current step.
     */
    val validTransitions: List<ValidTransition>
)