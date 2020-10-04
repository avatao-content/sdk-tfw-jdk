package com.avatao.tfw.sdk.api.data

enum class DeployStatus(
    val value: String
) {
    TODEPLOY("Pre-Deploy"),
    DEPLOYED("Deployed"),
    DEPLOYING("Deploying"),
    FAILED("Failed")
}