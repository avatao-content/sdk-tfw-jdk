package com.avatao.tfw.sdk

import com.avatao.tfw.sdk.api.data.DeployStatus
import com.avatao.tfw.sdk.api.data.EventKey
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

    sdk.onDeployStart {
        // CUSTOM DEPLOY LOGIC
        sdk.sendMessage().message("DEPLOY BUTTON CLICKED").commit()
        // You can pass an error message if needed.
        // Use no parameter if the deploy is successful.
        val errorMessage = "Error happened."
        sdk.signalDeployFailure(errorMessage)

        SubscriptionCommand.cancelSubscription() // ha nem akarjuk megtartani a felirakozast
        SubscriptionCommand.keepSubscription() // ha kell tovabbra is a feliratkozas
    }

    // deploy handler
    sdk.subscribe(EventKey.DEPLOY_START.value) { message ->

        // CUSTOM DEPLOY LOGIC
        sdk.sendMessage().message("DEPLOY BUTTON CLICKED").commit()
        // You can pass an error message if needed.
        // Use no parameter if the deploy is successful.
        val errorMessage = "Error happened."
        sdk.signalDeployFailure(errorMessage)

        SubscriptionCommand.cancelSubscription() // ha nem akarjuk megtartani a felirakozast
        SubscriptionCommand.keepSubscription() // ha kell tovabbra is a feliratkozas
    }

    sdk.subscribe("custom.valami") { message: TFWMessage ->

        // CUSTOM HANDLING LOGIC
        val buttonValue = message.data["value"]
        sdk.sendMessage("MESSAGE BUTTON CLICKED: " + buttonValue)

        SubscriptionCommand.keepSubscription()
    }

    sdk.send(
        TFWMessage.builder()
            .withIntent(TFWMessageIntent.CONTROL)
            .withKey("kulcs")
            .withScope(TFWMessageScope.BROADCAST)
            .withValue("kulcs", "ertek")
            .build()
    )


}
