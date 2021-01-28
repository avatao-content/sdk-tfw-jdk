package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.WebIDEAPI
import com.avatao.tfw.sdk.api.command.ReadFile
import com.avatao.tfw.sdk.api.command.WriteFile
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage

@Suppress("UNCHECKED_CAST")
class DefaultWebIDEAPI(
    private val connector: TFWServerConnector
) : WebIDEAPI {

    override fun selectFile(filename: String, patterns: List<String>?) {
        if (!patterns.isNullOrEmpty())
            connector.send(
                TFWMessage.builder()
                    .withKey(EventKey.IDE_READ.value)
                    .withValue(ReadFile::filename.name, filename)
                    .withValue(ReadFile::patterns.name, patterns)
                    .build()
            )
        else
            connector.send(
                TFWMessage.builder()
                    .withKey(EventKey.IDE_READ.value)
                    .withValue(ReadFile::filename.name, filename)
                    .build()
            )
    }

    override fun writeFile(filename: String, content: String) {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.IDE_WRITE.value)
                .withValue(WriteFile::filename.name, filename)
                .withValue(WriteFile::content.name, content)
                .build()
        )
    }

    override fun onWriteFile(fn: (WriteFile) -> SubscriptionCommand): Subscription {
        return connector.subscribe(EventKey.IDE_WRITE.value) {
            val cmd = WriteFile(
                connector = connector,
                data = it.data
            )
            fn(cmd)
        }
    }

    override fun onDeploy(fn: () -> SubscriptionCommand): Subscription {
        return connector.subscribe(EventKey.DEPLOY_START.value) {
            fn()
        }
    }

    override fun signalDeploySuccess() {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.DEPLOY_FINISH.value)
                .build()
        )
    }

    override fun signalDeployFailure(error: String) {
        connector.send(
            TFWMessage.builder()
                .withKey(EventKey.DEPLOY_FINISH.value)
                .withValue("error", error)
                .build()
        )
    }
}
