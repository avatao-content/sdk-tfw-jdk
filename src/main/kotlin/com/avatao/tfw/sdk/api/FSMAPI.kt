package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.data.FSMState
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription

interface FSMAPI {

    /**
     * Creates a new (linear) FSM in the TFW using the given [states].
     */
    fun createFSM(states: List<String>)

    /**
     * Attempts to trigger an FSM step.
     */
    fun step(state: Int = -1, force: Boolean = false)

    /**
     * Adds a callback, that will be called whenever the state of the FSM changes.
     */
    fun onStateChange(fn: (FSMState) -> SubscriptionCommand): Subscription
}
