package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.data.FSMState
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class DefaultFSMAPI(
    private val connector: TFWServerConnector
) : com.avatao.tfw.sdk.api.FSMAPI {

    override fun createFSM(states: List<String>) {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.FSM_CREATE.value)
                .withValue("states", states)
                .build()
        )
    }

    override fun step(state: Int, force: Boolean) {
        val stateToSend : String = if (state == -1)
            "step_next"
        else if (force)
            "to_$state"
        else
            "step_$state"
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.FSM_TRIGGER.value)
                .withValue("transition", stateToSend)
                .build()
        )
    }
    override fun onStateChange(fn: (FSMState) -> SubscriptionCommand): Subscription {
        return connector.subscribe(EventKey.FSM_UPDATE.value) {
            fn(Json{ ignoreUnknownKeys = true }.decodeFromString(it.rawJson))
        }
    }
}
