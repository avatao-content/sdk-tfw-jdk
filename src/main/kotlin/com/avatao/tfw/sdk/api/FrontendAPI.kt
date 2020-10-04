package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.builder.DashboardConfigurationBuilder
import com.avatao.tfw.sdk.api.builder.IDEConfigurationBuilder
import com.avatao.tfw.sdk.api.builder.SiteConfigurationBuilder
import com.avatao.tfw.sdk.api.data.DeployStatus
import com.avatao.tfw.sdk.api.data.FrontendLayout
import com.avatao.tfw.sdk.api.data.TerminalMenuItem
import com.avatao.tfw.sdk.connector.Subscription

interface FrontendAPI {

    fun configureSite(): SiteConfigurationBuilder

    fun configureDashboard(): DashboardConfigurationBuilder

    fun configureIDE(): IDEConfigurationBuilder

    /**
     * Callback that is called when the frontend signals that it is ready.
     * **Note that** the [Subscription] will automatically be cancelled
     * whenever the event is fired.
     */
    fun onFrontendReady(fn: (FrontendAPI) -> Unit): Subscription

    /**
     * When the user tries to reload the page, this will prompt a
     * confirmation dialog. It can be really useful, since the
     * frontend serves as a single-page application.
     */
    fun askReloadSite(askReloadSite: Boolean) = configureSite()
        .askReloadSite(askReloadSite)
        .commit()

    /**
     * Provides title for the browser tab.
     */
    fun setDocumentTitle(documentTitle: String) = configureSite()
        .documentTitle(documentTitle)
        .commit()

    /**
     * Switches the layout.
     */
    fun switchLayout(layout: FrontendLayout) = configureDashboard()
        .layout(layout)
        .commit()

    /**
     * Hides bot messages when true.
     */
    fun setHideMessages(hideMessages: Boolean) = configureDashboard()
        .hideMessages(hideMessages)
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
     * Reloads the iFrame of the frontend.
     */
    // TODO: this is not documented!
    fun reloadIframe(){
//        sendToTFW("dashboard.reloadIframe")
    }

}