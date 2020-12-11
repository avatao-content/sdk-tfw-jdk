package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.command.ReadFile
import com.avatao.tfw.sdk.api.command.WriteFile
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.FileContents
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription
import java.util.concurrent.Future

interface WebIDEAPI {

    /**
     * Tries to read the contents of the given [filename] using the given [patterns].
     */
    fun selectFile(
        filename: String,
        patterns: List<String>? = null
    ): Future<FileContents>

    /**
     * Adds an event handler that will be triggered whenever a file read operation
     * is requested in the web IDE.
     */
    fun onReadFile(
        fn: (ReadFile) -> SubscriptionCommand
    ): Subscription

    /**
     * Writes the given [content] to the given [filename].
     */
    fun writeFile(
        filename: String,
        content: String
    )

    /**
     * Adds an event handler that will be triggered whenever a file write operation
     * is requested in the web IDE.
     */
    fun onWriteFile(
        fn: (WriteFile) -> SubscriptionCommand
    ): Subscription

    /**
     * Informs the backend that the Deploy button was pressed in the IDE.
     */
    fun onDeployStart(fn: () -> SubscriptionCommand): Subscription

    /**
     * Signals that a deploy that was started after receiving an [EventKey.DEPLOY_START]
     * message has finished successfully.
     */
    fun signalDeploySuccess()

    /**
     * Signals that a deploy that was started after receiving an [EventKey.DEPLOY_START]
     * message has finished with the given [error].
     */
    fun signalDeployFailure(error: String)

}
