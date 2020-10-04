package com.avatao.tfw.sdk.api.builder

import com.avatao.tfw.sdk.api.FrontendAPI
import com.avatao.tfw.sdk.api.data.DeployStatus
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.impl.tryWithValue
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage

/**
 * Configures the IDE component on the frontend.
 */
class IDEConfigurationBuilder(
    private val connector: TFWServerConnector,
    private val frontendAPI: FrontendAPI,
    private var autoSaveInterval: Int? = null,
    private var showDeployButton: Boolean? = null,
    private val deployButtonText: MutableMap<DeployStatus, String> =
        DeployStatus.values().map { it to it.value }.toMap().toMutableMap()
) {

    /**
     * Saves the currently edited file at every [autoSaveInterval] seconds automatically.
     */
    fun autoSaveInterval(autoSaveInterval: Int) = also {
        this.autoSaveInterval = autoSaveInterval
    }

    /**
     * Displays the deploy button in the upper right corner of the IDE.
     */
    fun showDeployButton(showDeployButton: Boolean) = also {
        this.showDeployButton = showDeployButton
    }

    /**
     * 	Customizes the status message of the deploy button for a given [DeployStatus].
     */
    fun deployButtonText(deployStatus: DeployStatus, text: String) = also {
        this.deployButtonText[deployStatus] = text
    }

    /**
     * Builds and sends the configuration to TFW.
     */
    fun commit(): FrontendAPI {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.FRONTEND_IDE.value)
                .tryWithValue(autoSaveIntervalKey, autoSaveInterval)
                .tryWithValue(showDeployButtonKey, showDeployButton)
                .tryWithValue(deployButtonTextKey, deployButtonText)
                .build()
        )
        return frontendAPI
    }

    companion object {
        private val autoSaveIntervalKey = "autoSaveInterval"
        private val showDeployButtonKey = "showDeployButton"
        private val deployButtonTextKey = "deployButtonText"
    }
}