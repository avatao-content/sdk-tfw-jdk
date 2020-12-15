package com.avatao.tfw.sdk

import com.avatao.tfw.sdk.api.data.*
import com.avatao.tfw.sdk.message.TFWMessage
import com.avatao.tfw.sdk.message.TFWMessageIntent
import com.avatao.tfw.sdk.message.TFWMessageScope

fun main() {
    val sdk = TFW.create()

    /* ----------------------------- */
    /*        EVENT HANDLING         */
    /* ----------------------------- */

    /* DEFINING CUSTOM DEPLOY HANDLER */
    sdk.onDeploy {
        sdk.sendMessage("DEPLOY BUTTON CLICKED")
        sdk.signalDeploySuccess()

        // Alternatively you can send the error message when something is
        // wrong and the deploy was unsuccessful:
        // sdk.signalDeployFailure("Error happened.")

        // if you want to keep the subscription:
        SubscriptionCommand.keepSubscription()
        // if you don't want to keep the subscription:
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

    /* MESSAGING */
    sdk.sendMessage("Single message")

    sdk.sendMessage()
        .message("Single message with builder")
        .wpm(50)
        .originator("custom_originator")
        .buttons(listOf("yes","no"))
        .commit()

    // TODO
    sdk.queueMessages()
        .message(sdk.sendMessage().message("Queued message 1"))
        .message(sdk.sendMessage().message("Queued message 2").buttons(listOf("yes","no")))
        .commit()

    /* DASHBOARD */
    sdk.switchLayout(FrontendLayout.IDE_ONLY)
    sdk.askReloadSite(true)

    /* WEBSERVICE */
    sdk.setIframeURL("/webservice2")
    sdk.setShowUrlBar(true)
    sdk.reloadIframe()
    /* TERMINAL / CONSOLE */

    sdk.switchToConsole()
    sdk.switchToTerminal()
    sdk.writeToTerminal("terminal message")
    sdk.writeToConsole("console message")

    /* IDE */
    sdk.selectFile("TFWSDKExamples.kt")
    sdk.selectFile("/home/user/workdir/TFWSDKExamples.kt", listOf("/home/user/workdir/*.kt"))

    val labels = mapOf(
        DeployStatus.DEPLOYING to "Deploy0",
        DeployStatus.DEPLOYED to "Deployed0",
        DeployStatus.TODEPLOY to "Deploying...0",
        DeployStatus.FAILED to "Failed0"
    )
    sdk.setDeployButtonText(labels)
    sdk.writeFile("/home/user/workdir/TFWSDKExamples.kt", "Content changed.")
    sdk.setShowDeployButton(true)

    /* PROCESS MANAGEMENT */
    //sdk.startProcess("webservice")
    sdk.stopProcess("webservice")
    sdk.restartProcess("webservice")

    /* FSM */
    sdk.step() // steps to the next state
    sdk.step(3) // steps to the given state if the current state is the previous than given
    sdk.step(1, force = true) // steps to the given state no matter what is the current

    /* SENDING TFW MESSAGE WITH CUSTOM KEY */
    sdk.send(
        TFWMessage.builder()
            .withIntent(TFWMessageIntent.CONTROL)
            .withKey("key")
            .withScope(TFWMessageScope.BROADCAST)
            .withValue("key", "value")
            .build()
    )
}
