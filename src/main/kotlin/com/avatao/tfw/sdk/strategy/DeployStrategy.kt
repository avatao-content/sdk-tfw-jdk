package com.avatao.tfw.sdk.strategy

import com.avatao.tfw.sdk.api.TFWFacade
import com.avatao.tfw.sdk.api.builder.DeployStrategyBuilder
import java.io.Closeable

/**
 * Implements a deploy strategy for a project kind (like Maven or Gradle).
 */
interface DeployStrategy : Closeable {

    /**
     * Configures this [DeployStrategy] for the given [TFWFacade] instance.
     */
    fun configure(tfw: TFWFacade)

    companion object {

        /**
         * Creates a new [DeployStrategyBuilder].
         */
        @JvmStatic
        fun builder() = DeployStrategyBuilder()
    }
}
