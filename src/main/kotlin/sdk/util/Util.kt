/*
 * Copyright (C) 2020 Avatao.com Innovative Learning Kft.
 * All Rights Reserved. See LICENSE file for details.
 */
package sdk.util

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import java.io.File

class Util(private var serverConnector: TFWServerConnector) {

    /**
     * Sends the constructed message with the given key.
     * @param key
     * @param values
     */
    private fun sendToTFW(key: String, values: Map<String,String>? = null){
        val mapper = ObjectMapper()
        val triggerMessage = mapper.createObjectNode()
        triggerMessage.put("key", key)
        if (!values.isNullOrEmpty()) {
            values.forEach {
                triggerMessage.put(it.key, it.value)
            }
        }
        serverConnector.send(triggerMessage)
    }

    private fun sendToTFW(key: String, secondaryKey: String, values: Any){
        val mapper = ObjectMapper()
        val triggerMessage = mapper.createObjectNode()
        triggerMessage.put("key", key)
        if (values is ArrayNode)
            triggerMessage.put(secondaryKey, values)
        else if (values is ObjectNode)
            triggerMessage.put(secondaryKey, values)
        serverConnector.send(triggerMessage)
    }

    /**
     * When the user tries to reload the page, this will prompt a
     * confirmation dialog. It can be really useful, since the
     * frontend serves as a single-page application.
     */
    fun setReloadSite(value: Boolean) {
        sendToTFW("frontend.site", mapOf("askReloadSite" to value.toString()))
    }

    /**
     * Provides title for the browser tab.
     */
    fun setDocumentTitle(documentTitle: String) {
        sendToTFW("frontend.site", mapOf("documentTitle" to documentTitle))
    }

    /**
     * Default layout on challenge startup. See enabledLayouts for
     * available options. Available layouts:
     * - ide-web-vertical
     * - terminal-ide-web
     * - terminal-ide-vertical
     * - terminal-ide-horizontal
     * - terminal-only
     * - terminal-web
     * - ide-only
     * - web-only
     */
    fun switchLayout(layout: String){
        sendToTFW("frontend.dashboard", mapOf("layout" to layout))
    }

    /**
     * Hides bot messages when true.
     */
    fun setHideMessages(value: Boolean){
        sendToTFW("frontend.dashboard", mapOf("hideMessages" to value.toString()))
    }

    /**
     * The URL of the web service, make it empty when there is none.
     */
    fun setIframeURL(iframeUrl: String){
        sendToTFW("frontend.dashboard", mapOf("iframeUrl" to iframeUrl))
    }

    /**
     * Displays an additional URL bar to manipulate the web service.
     */
    fun setShowUrlBar(value: Boolean){
        sendToTFW("frontend.dashboard", mapOf("showUrlBar" to value.toString()))
    }

    /**
     * Selected tab on the terminal component to "console".
     */
    fun switchToConsole(){
        sendToTFW("frontend.dashboard", mapOf("terminalMenuItem" to "console"))
    }

    /**
     * Selected tab on the terminal component to "terminal".
     */
    fun switchToTerminal(){
        sendToTFW("frontend.dashboard", mapOf("terminalMenuItem" to "terminal"))
    }

    /**
     * Displays the deploy button in the upper right corner of the IDE.
     */
    fun setShowDeployButton(value: Boolean){
        sendToTFW("frontend.ide", mapOf("showDeployButton" to value.toString()))
    }

    /**
     * Configures the IDE component on the frontend.
     * Customizes the status messages of the deploy button.
     * It expects key-value pairs, where the possible keys are:
     * TODEPLOY, DEPLOYED, DEPLOYING, FAILED.
     * Important: You have set all of them, otherwise you will
     * see empty buttons in the undefined states.
     */
    fun setDeployButtonText(values: List<String>){
        if (values.size == 4){
            val mapper = ObjectMapper()
            val keyLabels = listOf("TODEPLOY", "DEPLOYED", "DEPLOYING", "FAILED")
            var valuesNode = mapper.createObjectNode()
            var i = 0
            values.forEach {
                valuesNode.put(keyLabels[i], it)
                i++
            }
            sendToTFW("frontend.ide","deployButtonText", valuesNode)
        }
    }

    /**
     * Reloads the iFrame of the frontend.
     */
    fun reloadIframe(){
        sendToTFW("dashboard.reloadIframe")
    }

    /**
     * Attempts to fetch the contents of a file, then loads it
     * in the IDE. You can also define new patterns for allowed
     * files in the setFilenamePatterns() function.
     * @param filename: Expects a list of glob patterns that will be matched against
     * the absolute paths of file system nodes. Important: symbolic
     * links are always resolved.
     */
    fun selectFile(filename: String, patterns: List<String>? = null){
        sendToTFW("ide.read", mapOf("filename" to filename))
        if (!patterns.isNullOrEmpty()) {
            val mapper = ObjectMapper()
            val patternArrayNode = mapper.createArrayNode()
            for (pattern in patterns) {
                patternArrayNode.add(pattern)
            }
            sendToTFW("ide.read","patterns", patternArrayNode)
        }
    }

    /**
     * Overwrites the contents of a file.
     * @filename: Full path or basename of a file.
     * @content: New contents of the selected file.
     */
    fun writeToFile(filename: String, content: String){
        sendToTFW("ide.write", mapOf("filename" to filename, "content" to content))
    }

    /**
     * Notifies the frontend that a watched file is being modified
     * on the file system, so it should update its state.
     */
    fun reloadIde() {
        sendToTFW("ide.reload", mapOf("scope" to "websocket"))
    }

    /**
     * The default action to deploy.start is to restart a service
     * and deployment is considered successful when the process
     * stays alive or exits with an expected status code.
     *
     * @param errorMessage (optional) The presence of this field determines the deployment's
     * status. When omitted, it is considered successful, otherwise
     * you can state the reason of the failure here. By default, this
     * is the message returned by the supervisor daemon.
     */
    fun finishDeploy(errorMessage: String? = null) {
        if (!errorMessage.isNullOrEmpty()) {
            sendToTFW("deploy.finish", mapOf("error" to errorMessage))
        }
        else {
            sendToTFW("deploy.finish")
        }
    }

    /**
     * Writes in the pseudo terminal that is connected with the frontend.
     * It is really useful when you need to pre-type a command for the user.
     */
    fun writeToTerminal(command: String) {
        sendToTFW("terminal.write", mapOf("command" to command))
    }

    /**
     * Updates the content of the console.
     */
    fun writeToConsole(content: String){
        sendToTFW("console.write", mapOf("content" to content))
    }

    /**
     * Retrieves the console's content.
     * You have to have an event handler subscribed with
     * the "console.read" key, too.
     */
    fun readConsole(){
        sendToTFW("console.read")
    }

    /**
     * Instructs the supervisor daemon to start a registered process.
     */
    fun startProcess(processName: String){
        sendToTFW("process.start", mapOf("name" to processName))
    }

    /*
     * Instructs the supervisor daemon to start a registered process.
     */
    fun stopProcess(processName: String){
        sendToTFW("process.stop", mapOf("name" to processName))
    }

    /**
     * Instructs the supervisor daemon to restart a registered process.
     */
    fun restartProcess(processName: String){
        sendToTFW("process.restart", mapOf("name" to processName))
    }

    /**
     * Attempts to trigger an FSM step.
     */
    fun stepTo(state: Int) {
        sendToTFW("fsm.trigger", mapOf("transition" to "to_$state"))
    }

    /**
     * Attempts to trigger an FSM step.
     */
    fun step(state: Int) {
        sendToTFW("fsm.trigger", mapOf("transition" to "step_$state"))
    }

    /**
     * Send a message to an event handler.
     * This envelopes the desired message in the 'values' field
     * of the message to TFWServer.
     */
    fun sendCustomTFWMessage(key: String, values: Map<String,String>) {
        sendToTFW(key, values)
    }

    fun getValueFromJSONMessage(message: String, key: String): String {
        val mapper = ObjectMapper()
        val factory: JsonFactory = mapper.jsonFactory
        val parser: JsonParser = factory.createJsonParser(message)
        val actualObj: JsonNode = mapper.readTree(parser)
        return actualObj[key].toString().replace("\"","")
    }


}