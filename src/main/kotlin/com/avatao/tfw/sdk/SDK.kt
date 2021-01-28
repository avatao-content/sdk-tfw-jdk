package com.avatao.tfw.sdk

import com.avatao.tfw.sdk.api.impl.DefaultTFWFacade
import com.avatao.tfw.sdk.api.TFWFacade

object SDK {
    @JvmStatic
    fun create(): TFWFacade = DefaultTFWFacade()
}