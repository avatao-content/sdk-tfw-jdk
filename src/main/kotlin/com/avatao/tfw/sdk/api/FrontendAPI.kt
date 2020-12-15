package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.builder.DashboardConfigurationBuilder
import com.avatao.tfw.sdk.api.builder.IDEConfigurationBuilder
import com.avatao.tfw.sdk.api.builder.SiteConfigurationBuilder
import com.avatao.tfw.sdk.api.data.DeployStatus
import com.avatao.tfw.sdk.api.data.FrontendLayout
import com.avatao.tfw.sdk.api.data.TerminalMenuItem

interface FrontendAPI {

    fun configureSite(): SiteConfigurationBuilder

    fun configureDashboard(): DashboardConfigurationBuilder

    fun configureIDE(): IDEConfigurationBuilder

    /**
     * When the user tries to reload the page, this will prompt a
     * confirmation dialog. It can be really useful, since the
     * frontend serves as a single-page application.
     */
    fun askReloadSite(askReloadSite: Boolean) = configureSite()
        .askReloadSite(askReloadSite)
        .commit()

    /**
     * Switches the layout.
     */
    fun switchLayout(layout: FrontendLayout) = configureDashboard()
        .layout(layout)
        .commit()

    /**
     * Sets the URL of the web service. Set it to empty when there is none.
     */
    fun setIframeURL(iframeUrl: String) = configureDashboard()
        .iframeUrl(iframeUrl)
        .commit()

    /**
     * Displays an additional URL bar to manipulate the web service.
     */
    fun setShowUrlBar(showUrlBar: Boolean) = configureDashboard()
        .showUrlBar(showUrlBar)
        .commit()

    /**
     * Sets the selected tab on the terminal component to "console".
     */
    fun switchToConsole() = configureDashboard()
        .terminalMenuItem(TerminalMenuItem.CONSOLE)
        .commit()

    /**
     * Sets the selected tab on the terminal component to "terminal".
     */
    fun switchToTerminal() = configureDashboard()
        .terminalMenuItem(TerminalMenuItem.TERMINAL)
        .commit()

    /**
     * Displays the deploy button in the upper right corner of the IDE.
     */
    fun setShowDeployButton(showDeployButton: Boolean) = configureIDE()
        .showDeployButton(showDeployButton)
        .commit()

    /**
     * Configures the IDE component on the frontend.
     * Customizes the status messages of the deploy button.
     * It expects key-value pairs, where the possible keys are:
     * TODEPLOY, DEPLOYED, DEPLOYING, FAILED.
     */
    fun setDeployButtonText(values: Map<DeployStatus, String>) = configureIDE().apply {
        values.forEach { status, label ->
            deployButtonText(status, label)
        }
    }.commit()

    /**
     * Reloads the iFrame of the webservice's iframe.
     */
    fun reloadIframe()

}
