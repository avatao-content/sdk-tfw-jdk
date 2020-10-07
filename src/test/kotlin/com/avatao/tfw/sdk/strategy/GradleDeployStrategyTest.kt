package com.avatao.tfw.sdk.strategy

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

@DisplayName("Given a GradleDeployStrategy")
class GradleDeployStrategyTest {

    private lateinit var target: GradleDeployStrategy

    private val facadeStub: TFWFacadeStub = TFWFacadeStub()

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        target = GradleDeployStrategy(
            healthCheckUrl = "http://localhost:8080/healthy",
            startupTimeoutMs = 15000,
            runCommand = "gradlew.bat bootRun"
        )
    }

    @AfterEach
    fun tearDown() {
        target.close()
    }

    @Test
    @DisplayName("When used with example project it should start properly")
    fun shouldRunWithExampleProject() {
        val latch = CountDownLatch(1)

        target.configure(facadeStub)

        facadeStub.deployStartCallbacks.first().invoke()

        var running = true
        thread {
            while (running) {
                if (facadeStub.deploySuccess.get() !== null) {
                    running = false
                }
            }
        }

        println("Awaiting test completion.")
        latch.await(30, TimeUnit.SECONDS)
        running = false

        assert(facadeStub.deploySuccess.get() == true)

    }
}
