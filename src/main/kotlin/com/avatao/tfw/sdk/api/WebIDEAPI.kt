package com.avatao.tfw.sdk.api

import com.avatao.tfw.sdk.api.command.ReadFile
import com.avatao.tfw.sdk.api.command.WriteFile
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.connector.Subscription
import com.fasterxml.jackson.databind.ObjectMapper

interface WebIDEAPI {

    /**
     * Adds an event handler that will be triggered whenever a file read operation
     * is requested in the web IDE.
     */
    fun onReadFile(
        fn: (ReadFile) -> SubscriptionCommand
    ): Subscription

    /**
     * Adds an event handler that will be triggered whenever a file write operation
     * is requested in the web IDE.
     */
    fun onWriteFile(
        fn: (WriteFile) -> SubscriptionCommand
    ): Subscription

    /**
     * Notifies the frontend that a watched file is being modified on the file system,
     * so it should update its state.
     */
    fun reloadIde()

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

    /**
     * Attempts to fetch the contents of a file, then loads it
     * in the IDE. You can also define new patterns for allowed
     * files in the setFilenamePatterns() function.
     * @param filename: Expects a list of glob patterns that will be matched against
     * the absolute paths of file system nodes. Important: symbolic
     * links are always resolved.
     */
    // TODO: so how is this supposed to work?
    fun selectFile(filename: String, patterns: List<String>? = null){
//        sendToTFW("ide.read", mapOf("filename" to filename))
//        if (!patterns.isNullOrEmpty()) {
//            val mapper = ObjectMapper()
//            val patternArrayNode = mapper.createArrayNode()
//            for (pattern in patterns) {
//                patternArrayNode.add(pattern)
//            }
//            sendToTFW("ide.read","patterns", patternArrayNode)
//        }
    }

    /**
     * Overwrites the contents of a file.
     * @filename: Full path or basename of a file.
     * @content: New contents of the selected file.
     */
    // TODO: so how is this supposed to work?
    fun writeToFile(filename: String, content: String){
//        sendToTFW("ide.write", mapOf("filename" to filename, "content" to content))
    }

}