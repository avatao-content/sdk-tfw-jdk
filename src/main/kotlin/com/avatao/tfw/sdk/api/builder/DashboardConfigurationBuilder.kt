package com.avatao.tfw.sdk.api.builder

import com.avatao.tfw.sdk.api.FrontendAPI
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.FrontendLayout
import com.avatao.tfw.sdk.api.data.TerminalMenuItem
import com.avatao.tfw.sdk.api.impl.tryWithValue
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage

/**
 * Configures the dashboard component on the frontend.
 */
class DashboardConfigurationBuilder(
    private val connector: TFWServerConnector,
    private val frontendAPI: FrontendAPI,
    private var layout: FrontendLayout? = null,
    private var hideMessages: Boolean? = null,
    private var iframeUrl: String? = null,
    private var showUrlBar: Boolean? = null,
    private var terminalMenuItem: TerminalMenuItem? = null,
    private var enabledLayouts: List<FrontendLayout>? = null
) {

    /**
     * Default layout on challenge startup. See enabledLayouts for available options.
     */
    fun layout(layout: FrontendLayout) = also {
        this.layout = layout
    }

    /**
     * Hides bot messages when `true`.
     */
    fun hideMessages(hideMessages: Boolean) = also {
        this.hideMessages = hideMessages
    }

    /**
     * The URL of the web service, make it empty when there is none.
     */
    fun iframeUrl(iframeUrl: String) = also {
        this.iframeUrl = iframeUrl
    }

    /**
     * Displays an additional URL bar to manipulate the web service.
     */
    fun showUrlBar(showUrlBar: Boolean) = also {
        this.showUrlBar = showUrlBar
    }

    /**
     * Selected tab on the terminal component: "terminal" or "console".
     */
    fun terminalMenuItem(terminalMenuItem: TerminalMenuItem) = also {
        this.terminalMenuItem = terminalMenuItem
    }

    /**
     * List of layouts on the right side of the page: terminal-ide-web, terminal-ide-vertical, terminal-web,
     * ide-web-vertical, terminal-ide-horizontal, terminal-only, ide-only, web-only.
     */
    fun enabledLayouts(enabledLayouts: List<FrontendLayout>) = also {
        this.enabledLayouts = enabledLayouts
    }

    /**
     * Builds and sends the configuration to TFW.
     */
    fun commit(): FrontendAPI {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.FRONTEND_DASHBOARD.value)
                .tryWithValue(layoutKey, layout?.value)
                .tryWithValue(hideMessagesKey, hideMessages)
                .tryWithValue(iframeUrlKey, iframeUrl)
                .tryWithValue(showUrlBarKey, showUrlBar)
                .tryWithValue(terminalMenuItemKey, terminalMenuItem)
                .tryWithValue(enabledLayoutsKey, enabledLayouts)
                .build()
        )
        return frontendAPI
    }

    companion object {
        private val layoutKey = "layout"
        private val hideMessagesKey = "hideMessages"
        private val iframeUrlKey = "iframeUrl"
        private val showUrlBarKey = "showUrlBar"
        private val terminalMenuItemKey = "terminalMenuItem"
        private val enabledLayoutsKey = "enabledLayouts"
    }
}