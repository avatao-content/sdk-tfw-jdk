package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.FSMAPI
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage

class DefaultFSMAPI(
    private val connector: TFWServerConnector
) : FSMAPI {

    override fun transitionTo(step: String) {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.FSM_TRIGGER.value)
                .withValue("transition", step)
                .build()
        )
    }
}