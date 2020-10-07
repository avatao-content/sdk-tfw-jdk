package com.avatao.tfw.sdk

import com.avatao.tfw.sdk.api.data.CancelSubscription
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.message.TFWMessage
import com.avatao.tfw.sdk.mock.TFWServerMock
import com.avatao.tfw.sdk.strategy.DeployStrategy
import java.io.File
import java.util.concurrent.CountDownLatch


fun main() {

    val latch = CountDownLatch(1)

    val mock = TFWServerMock()
    val api = TFW.create()

    val states = listOf("start", "finish")

    // triggers next step on success
    val onSuccess = {
        api.trigger("finish")
    }

    // writes to console on failure
    val onFailure = { msg: String ->
        api.writeToConsole(msg)
        println("Test failed: $msg")
        latch.countDown()
    }

    api.useDeployStrategy(
        DeployStrategy.builder()
            .runCommand("./gradlew bootRun --stacktrace --no-daemon")
            .startupTimeoutMs(45000)
            .healthCheckUrl("http://localhost:8080/healthy")
            .onSuccess(onSuccess)
            .onFailure(onFailure)
            .buildForGradle()
    )

    api.createFSM(states)

    // we can terminate in end state
    api.onUpdate {
        latch.countDown()
        CancelSubscription
    }

    // starts the deploy
    api.connector.send(
        TFWMessage.builder()
            .withKey(EventKey.DEPLOY_START.value)
            .build()
    )


    latch.await()

    api.close()
    mock.close()
}
