package com.avatao.tfw.sdk.api.builder

import com.avatao.tfw.sdk.strategy.GradleDeployStrategy

class DeployStrategyBuilder(
    private var startupTimeoutMs: Long = 10000,
    private var runCommand: String? = null,
    private var healthCheckUrl: String? = null,
    private var onSuccess: () -> Unit = {},
    private var onFailure: (String) -> Unit = {}
) {

    fun healthCheckUrl(healthCheckUrl: String) = also {
        this.healthCheckUrl = healthCheckUrl
    }

    fun startupTimeoutMs(startupTimeoutMs: Long) = also {
        this.startupTimeoutMs = startupTimeoutMs
    }

    fun runCommand(runCommand: String) = also {
        this.runCommand = runCommand
    }

    fun onSuccess(onSuccess: () -> Unit) = also {
        this.onSuccess = onSuccess
    }

    fun onFailure(onFailure: (String) -> Unit) = also {
        this.onFailure = onFailure
    }

    fun buildForGradle(): GradleDeployStrategy {
        require(runCommand !== null) {
            "runCommand can't be empty"
        }
        require(healthCheckUrl !== null) {
            "healthCheckUrl can't be empty"
        }
        return GradleDeployStrategy(
            startupTimeoutMs = startupTimeoutMs,
            healthCheckUrl = healthCheckUrl!!,
            runCommand = runCommand!!,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

}
