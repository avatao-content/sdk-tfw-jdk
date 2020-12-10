package com.avatao.tfw.sdk

import com.avatao.tfw.sdk.api.data.DeployStatus
import com.avatao.tfw.sdk.api.data.FrontendLayout

fun main() {

    val sdk = TFW.create()

    /* MESSAGING */
    sdk.sendMessage()
        .message("First message")
        .wpm(50)
        .originator("mittom")
        .commit()

    sdk.queueMessages()
        .message(sdk.sendMessage().message("wom"))
        .message(sdk.sendMessage().message("xul"))
        .commit()

    /* DASHBOARD */
    sdk.switchLayout(FrontendLayout.IDE_ONLY)
    sdk.askReloadSite(true)

    /* WEBSERVICE */
    sdk.setIframeURL("http://foo.bar")
    sdk.setShowUrlBar(true)
    sdk.reloadIframe()

    /* TERMINAL / CONSOLE */
    sdk.switchToConsole()
    sdk.switchToTerminal()
    sdk.writeToTerminal("rm -Rf /")
    sdk.writeToConsole("no problemo")

    /* IDE */
    // select file?
    sdk.readFile("x.java", listOf())

    val patterns = listOf("/home/user/workdir/*")
    sdk.readFile("/home/user/workdir/cat2.txt", patterns)

    val labels = mapOf(
        DeployStatus.DEPLOYING to "Deploy",
        DeployStatus.DEPLOYED to "Deployed",
        DeployStatus.TODEPLOY to "Deploying...",
        DeployStatus.FAILED to "Failed"
    )
    sdk.setDeployButtonText(labels)
    sdk.writeFile("/home/user/workdir/cat.txt", "Content overrided")
    sdk.setShowDeployButton(false)
    sdk.signalDeploySuccess()

    /* PROCESS MANAGEMENT */
    sdk.startProcess("webservice")
    sdk.stopProcess("webservice")
    sdk.restartProcess("webservice")

    /* FSM */
    sdk.trigger("2") // steps to the given state


}
