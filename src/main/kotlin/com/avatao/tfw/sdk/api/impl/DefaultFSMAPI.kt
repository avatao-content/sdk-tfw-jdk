package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.FSMAPI
import com.avatao.tfw.sdk.api.data.CancelSubscription
import com.avatao.tfw.sdk.api.data.CurrentFSMState
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class DefaultFSMAPI(
    private val connector: TFWServerConnector
) : FSMAPI {

    override fun createFSM(states: List<String>) {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.FSM_CREATE.value)
                .withValue("states", states)
                .build()
        )
    }

    override fun trigger(state: String) {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.FSM_TRIGGER.value)
                .withValue("transition", state)
                .build()
        )
    }

    override fun getCurrentState(): Future<CurrentFSMState> {
        val result = CompletableFuture<CurrentFSMState>()
        connector.subscribe(EventKey.FSM_UPDATE.value) {
            result.complete(Json.decodeFromString<CurrentFSMState>(it.rawJson))
            CancelSubscription
        }
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.FSM_UPDATE.value)
                .build()
        )
        return result
    }

    override fun onUpdate(fn: (CurrentFSMState) -> SubscriptionCommand): Subscription {
        return connector.subscribe(EventKey.FSM_UPDATE.value) {
            fn(Json.decodeFromString(it.rawJson))
        }
    }


}
