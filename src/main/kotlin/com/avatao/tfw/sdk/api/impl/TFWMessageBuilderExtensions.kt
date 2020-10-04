package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.message.TFWMessageBuilder

fun TFWMessageBuilder.tryWithValue(key: String, value: Any?) = also {
    value?.let {
        this.withValue(key, it)
    }
}