/*
 * Copyright (C) 2020 Avatao.com Innovative Learning Kft.
 * All Rights Reserved. See LICENSE file for details.
 */
package sdk

import sdk.eventhandling.customhandlers.*
import sdk.util.MessageSender
import sdk.util.TFWServerConnector
import sdk.util.Util

class API {
    private val serverConnector : TFWServerConnector = TFWServerConnector()
    private val util = Util(serverConnector)
    private val messageSender = MessageSender(serverConnector)
    private var actualState = 0

    fun getConnector(): TFWServerConnector {
        return serverConnector
    }

    fun closeAPIContext(){
        serverConnector.closeContext()
    }

    fun getDeployEventHandler(handleEvent: ((String?)) -> Unit): DeployEventHandler {
        return DeployEventHandler(this, handleEvent)
    }

    fun getConsoleReadEventHandler(handleEvent: (String) -> Unit): ConsoleReadEventHandler {
        return ConsoleReadEventHandler(this, handleEvent)
    }

    fun getCustomEventHandler(key: String, handleEvent: (String) -> Unit): CustomEventHandler {
        return CustomEventHandler(key, this, handleEvent)
    }

    fun getActualState() : Int {
        Thread.sleep(1000) // step to the next state takes some time
        return actualState
    }

    fun getFSMStepsEventHandler(handleEvent: ((String) -> Unit)? = null): FSMStepsEventHandler? {
        val handleEventWithUpdatedState: (String) -> Unit = {
            message: String ->
            run {
                this.actualState = Integer.parseInt(getValueFromJSONMessage(message, "current_state"))
                handleEvent?.invoke(message)
            }
        }
        return FSMStepsEventHandler(this, handleEventWithUpdatedState)
    }

    fun getHistoryMonitorEventHandler(handleEvent: (String) -> Unit): HistoryMonitorEventHandler {
        return HistoryMonitorEventHandler(this, handleEvent)
    }

    fun getTypingEventHandler(handleEvent: (String) -> Unit): TypingEventHandler {
        return TypingEventHandler(this, handleEvent)
    }

    /**
     * Sends a message.
     * @param message message to send
     */
    fun sendMessage(message: String, originator: String = "avataobot"){
        messageSender.send(message, originator)
    }

    /**
     * Queue a list of messages to be displayed in a chatbot-like manner.
     * @param originator name of sender to be displayed on the frontend
     * @param messages list of messages to queue
     */
    fun queueMessages(messages: List<String>, originator: String = "avataobot"){
        messageSender.queueMessages(messages, originator)
    }

    /**
     * When the user tries to reload the page, this will prompt a
     * confirmation dialog. It can be really useful, since the
     * frontend serves as a single-page application.
     */
    fun setReloadSite(value: Boolean) {
        util.setReloadSite(value)
    }

    /**
     * Provides title for the browser tab.
     */
    fun setDocumentTitle(documentTitle: String) {
        util.setDocumentTitle(documentTitle)
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
        util.switchLayout(layout)
    }

    /**
     * Hides bot messages when true.
     */
    fun setHideMessages(value: Boolean){
        util.setHideMessages(value)
    }

    /**
     * The URL of the web service, make it empty when there is none.
     */
    fun setIframeURL(iframeUrl: String){
        util.setIframeURL(iframeUrl)
    }

    /**
     * Displays an additional URL bar to manipulate the web service.
     */
    fun setShowUrlBar(value: Boolean){
        util.setShowUrlBar(value)
    }

    /**
     * Selected tab on the terminal component to "console".
     */
    fun switchToConsole(){
        util.switchToConsole()
    }

    /**
     * Selected tab on the terminal component to "terminal".
     */
    fun switchToTerminal(){
        util.switchToTerminal()
    }

    /**
     * Displays the deploy button in the upper right corner of the IDE.
     */
    fun setShowDeployButton(value: Boolean){
        util.setShowDeployButton(value)
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
        util.setDeployButtonText(values)
    }

    /**
     * Reloads the iFrame of the frontend.
     */
    fun reloadIframe(){
        util.reloadIframe()
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
        util.selectFile(filename, patterns)
    }

    /**
     * Overwrites the contents of a file.
     * @filename: Full path or basename of a file.
     * @content: New contents of the selected file.
     */
    fun writeToFile(filename: String, content: String){
        util.writeToFile(filename,content)
    }

    /**
     * Notifies the frontend that a watched file is being modified
     * on the file system, so it should update its state.
     */
    fun reloadIde() {
        util.reloadIde()
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
        util.finishDeploy(errorMessage)
    }

    /**
     * Writes in the pseudo terminal that is connected with the frontend.
     * It is really useful when you need to pre-type a command for the user.
     */
    fun writeToTerminal(command: String) {
        util.writeToTerminal(command)
    }

    /**
     * Updates the content of the console.
     */
    fun writeToConsole(content: String){
        util.writeToConsole(content)
    }

    /**
     * Retrieves the console's content.
     * You have to have an event handler subscribed with
     * the "console.read" key, too.
     */
    fun readConsole(){
        util.readConsole()
    }

    /**
     * Instructs the supervisor daemon to start a registered process.
     */
    fun startProcess(processName: String){
        util.startProcess(processName)
    }

    /*
     * Instructs the supervisor daemon to start a registered process.
     */
    fun stopProcess(processName: String){
        util.stopProcess(processName)
    }

    /**
     * Instructs the supervisor daemon to restart a registered process.
     */
    fun restartProcess(processName: String){
        util.restartProcess(processName)
    }

    /**
     * Attempts to trigger an FSM step.
     */
    fun stepTo(state: Int) {
        util.stepTo(state)
    }

    /**
     * Attempts to trigger an FSM step.
     */
    fun step(state: Int) {
        util.step(state)
    }

    /**
     * Send a message to an event handler.
     * This envelopes the desired message in the 'values' field
     * of the message to TFWServer.
     */
    fun sendCustomTFWMessage(key: String, values: Map<String,String>) {
        util.sendCustomTFWMessage(key,values)
    }

    fun getValueFromJSONMessage(message: String, key: String) : String{
        return util.getValueFromJSONMessage(message, key)
    }

}