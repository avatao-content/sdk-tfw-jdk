package com.avatao.tfw.sdk.api

interface FSMAPI {

    /**
     * Attempts to transition the FSM to the given step.
     */
    fun transitionTo(step: String)

    // TODO: these are not documented!
    /**
     * Attempts to trigger an FSM step.
     */
    fun stepTo(state: Int) {
//        sendToTFW("fsm.trigger", mapOf("transition" to "to_$state"))
    }

    /**
     * Attempts to trigger an FSM step.
     */
    fun step(state: Int) {
//        sendToTFW("fsm.trigger", mapOf("transition" to "step_$state"))
    }
}