package com.avatao.tfw.sdk.api.impl

import com.avatao.tfw.sdk.api.WebIDEAPI
import com.avatao.tfw.sdk.api.command.ReadFile
import com.avatao.tfw.sdk.api.command.WriteFile
import com.avatao.tfw.sdk.api.data.CancelSubscription
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.FileContents
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription
import com.avatao.tfw.sdk.connector.TFWServerConnector
import com.avatao.tfw.sdk.message.TFWMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

@Suppress("UNCHECKED_CAST")
class DefaultWebIDEAPI(
    private val connector: TFWServerConnector
) : WebIDEAPI {

    override fun selectFile(filename: String, patterns: List<String>?): Future<FileContents> {
        val result = CompletableFuture<FileContents>()
        connector.subscribe(EventKey.IDE_READ.value) {
            val prr = Json { ignoreUnknownKeys = true }.decodeFromString<FileContents>(it.rawJson)
            result.complete(prr)
            CancelSubscription
        }

        if (!patterns.isNullOrEmpty())
            connector.send(
                TFWMessage.builder()
                    .withKey(EventKey.IDE_READ.value)
                    .withValue(ReadFile::filename.name, filename)
                    .withValue(ReadFile::patterns.name, filename)
                    .build()
            )
        else
            connector.send(
                TFWMessage.builder()
                    .withKey(EventKey.IDE_READ.value)
                    .withValue(ReadFile::filename.name, filename)
                    .build()
            )
        return result
    }

    override fun onReadFile(fn: (ReadFile) -> SubscriptionCommand): Subscription {
        return connector.subscribe(EventKey.IDE_READ.value) {
            val cmd = ReadFile(
                connector = connector,
                data = it.data
            )
            fn(cmd)
        }
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
