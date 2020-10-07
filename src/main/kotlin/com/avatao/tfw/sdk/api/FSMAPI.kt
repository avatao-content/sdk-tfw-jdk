package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.data.CurrentFSMState
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription
import java.util.concurrent.Future

interface FSMAPI {

    /**
     * Creates a new (linear) FSM in the TFW using the given [states].
     */
    fun createFSM(states: List<String>)

    /**
     * Attempts to trigger an FSM step.
     */
    fun trigger(state: String)

    /**
     * Inquires about the current state of the FSM.
     */
    fun getCurrentState(): Future<CurrentFSMState>

    /**
     * Adds a callback, that will be called whenever the state of the FSM changes.
     */
    fun onUpdate(fn: (CurrentFSMState) -> SubscriptionCommand): Subscription
}
