package com.avatao.tfw.sdk.strategy

import com.avatao.tfw.sdk.connector.Subscription

class SubscriptionStub : Subscription {

    override val key: String
        get() = ""

    override fun close() {
    }
}
