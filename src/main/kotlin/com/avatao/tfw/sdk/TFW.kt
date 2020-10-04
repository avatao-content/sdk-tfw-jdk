package com.avatao.tfw.sdk

import com.avatao.tfw.sdk.api.DefaultTFWFacade
import com.avatao.tfw.sdk.api.TFWFacade

object TFW {

    @JvmStatic
    fun create(): TFWFacade = DefaultTFWFacade()
}