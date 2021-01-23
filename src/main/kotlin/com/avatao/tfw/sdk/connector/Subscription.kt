package com.avatao.tfw.sdk.connector

import java.io.Closeable

interface Subscription : Closeable {
    val key: String
}