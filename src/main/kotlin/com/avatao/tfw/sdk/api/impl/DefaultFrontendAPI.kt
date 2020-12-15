package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.FrontendAPI
import com.avatao.tfw.sdk.api.builder.DashboardConfigurationBuilder
import com.avatao.tfw.sdk.api.builder.IDEConfigurationBuilder
import com.avatao.tfw.sdk.api.builder.SiteConfigurationBuilder
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage

class DefaultFrontendAPI(
    private val connector: TFWServerConnector
) : FrontendAPI {

    override fun configureSite() = SiteConfigurationBuilder(connector, this)

    override fun configureDashboard() = DashboardConfigurationBuilder(connector, this)

    override fun configureIDE() = IDEConfigurationBuilder(connector, this)

    override fun reloadIframe() = connector.send(
        TFWMessage.builder()
            .withKey(EventKey.RELOAD_IFRAME.value)
            .build()
    )
}
