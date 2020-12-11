package com.avatao.tfw.sdk

import com.avatao.tfw.sdk.api.data.DeployStatus
import com.avatao.tfw.sdk.api.data.FrontendLayout
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.message.TFWMessage
import com.avatao.tfw.sdk.message.TFWMessageIntent
import com.avatao.tfw.sdk.message.TFWMessageScope

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
    sdk.selectFile("Program.java")
    sdk.selectFile("/home/user/workdir/Program.java", listOf("/home/user/workdir/*.java"))

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
    sdk.step() // steps to the next state
    sdk.step(2) // steps to the given state if the current state is the previous than given
    sdk.step(3, force = true) // steps to the given state no matter what is the current

    /* SENDING CUSTOM TFW MESSAGE */
    sdk.send(
        TFWMessage.builder()
            .withIntent(TFWMessageIntent.CONTROL)
            .withKey("key")
            .withScope(TFWMessageScope.BROADCAST)
            .withValue("key", "value")
            .build()
    )

    /* -----------------------------*/
    /*        EVENT HANDLING        */
    /* -----------------------------*/

    /* DEFINING CUSTOM DEPLOY HANDLER */
    sdk.onDeployStart {
        sdk.sendMessage("DEPLOY BUTTON CLICKED")
        sdk.signalDeploySuccess()

        // Alternatively you can send the error message when something is wrong and
        // the deploy was unsuccessful
        // val errorMessage = "Error happened."
        // sdk.signalDeployFailure(errorMessage)

        // if you want to keep the subscription
        SubscriptionCommand.keepSubscription()
        // if you don't want to keep the subscription
        // SubscriptionCommand.cancelSubscription()
    }

    /* DEFINING TERMINAL COMMAND HANDLER */
    sdk.onTerminalCommand { command ->
        sdk.sendMessage("COMMAND EXECUTED: $command")
        SubscriptionCommand.keepSubscription()
    }

    /* DEFINING IDE WRITE HANDLER */
    sdk.onWriteFile {
        sdk.sendMessage("THE CONTENT OF THE FILE IN THE IDE HAS CHANGED")
        SubscriptionCommand.keepSubscription()
    }

    /* STATE CHANGE HANDLER */
    sdk.onStateChange {
        sdk.sendMessage("CURRENT STATE: " + sdk.getCurrentState())
        SubscriptionCommand.keepSubscription()
    }

    /* DEFINE BUTTON CLICK HANDLER */
    sdk.onButtonClickHandler { button ->
        sdk.sendMessage("MESSAGE BUTTON CLICKED: $button")
        SubscriptionCommand.keepSubscription()
    }

    /* DEFINING CUSTOM HANDLER WITH ARBITRARY KEY */
    sdk.subscribe("custom.key") { message: TFWMessage ->
        sdk.sendMessage("HANDLING EVENT WITH KEY 'custom.key'. MESSAGE: $message")
        SubscriptionCommand.keepSubscription()
    }
}
