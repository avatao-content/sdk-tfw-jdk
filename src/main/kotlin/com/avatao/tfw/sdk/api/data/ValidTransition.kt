package com.avatao.tfw.sdk.api.data

import kotlinx.serialization.Serializable

@Serializable
data class ValidTransition(
    val trigger: String
)