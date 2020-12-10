package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.FrontendAPI
import com.avatao.tfw.sdk.api.builder.DashboardConfigurationBuilder
import com.avatao.tfw.sdk.api.builder.IDEConfigurationBuilder
import com.avatao.tfw.sdk.api.builder.SiteConfigurationBuilder
import com.avatao.tfw.sdk.api.data.CancelSubscription
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.connector.Subscription
import com.avatao.tfw.sdk.connector.TFWServerConnector

class DefaultFrontendAPI(
    private val connector: TFWServerConnector
) : FrontendAPI {

    override fun configureSite() = SiteConfigurationBuilder(connector, this)

    override fun configureDashboard() = DashboardConfigurationBuilder(connector, this)

    override fun configureIDE() = IDEConfigurationBuilder(connector, this)

}
