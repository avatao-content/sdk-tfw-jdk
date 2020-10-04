package com.avatao.tfw.sdk.api.data

sealed class SubscriptionCommand {

    companion object {

        @JvmStatic
        fun keepSubscription() = KeepSubscription

        @JvmStatic
        fun cancelSubscription() = CancelSubscription
    }
}

object CancelSubscription : SubscriptionCommand()

object KeepSubscription : SubscriptionCommand()