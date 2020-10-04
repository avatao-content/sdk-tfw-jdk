package com.avatao.tfw.sdk.api.builder

import com.avatao.tfw.sdk.api.FrontendAPI
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage

/**
 * Configures the site's main properties.
 */
class SiteConfigurationBuilder(
    private val connector: TFWServerConnector,
    private val frontendAPI: FrontendAPI,
    private var askReloadSite: Boolean? = null,
    private var documentTitle: String? = null
) {

    /**
     * When the user tries to reload the page, this will prompt a confirmation dialog.
     * It can be really useful, since the frontend serves as a single-page application.
     */
    fun askReloadSite(askReloadSite: Boolean) = also {
        this.askReloadSite = askReloadSite
    }

    /**
     * Provides the title for the browser tab.
     */
    fun documentTitle(documentTitle: String) = also {
        this.documentTitle = documentTitle
    }

    /**
     * Builds and sends the configuration to TFW.
     */
    fun commit(): FrontendAPI {
        connector.send(TFWMessage.builder()
            .withKey(EventKey.FRONTEND_SITE.value).apply {
                askReloadSite?.let { withValue(askReloadSiteKey, it.toString()) }
                documentTitle?.let { withValue(documentTitlekey, it) }
            }
            .build())
        return frontendAPI
    }

    companion object {
        private val askReloadSiteKey = "askReloadSite"
        private val documentTitlekey = "documentTitle"
    }
}