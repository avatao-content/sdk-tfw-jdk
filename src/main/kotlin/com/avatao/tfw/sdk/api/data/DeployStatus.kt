package com.avatao.tfw.sdk.api.data

enum class DeployStatus(
    val value: String
) {
    TODEPLOY("Deploy"),
    DEPLOYED("Deployed"),
    DEPLOYING("Deploying"),
    FAILED("Failed")
}