package com.avatao.tfw.sdk.strategy

import com.avatao.tfw.sdk.api.TFWFacade
import com.avatao.tfw.sdk.api.data.KeepSubscription
import org.apache.http.HttpResponse
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import java.io.Closeable
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference


@Suppress("BlockingMethodInNonBlockingContext")
class GradleDeployStrategy(
    private val runCommand: String,
    private val healthCheckUrl: String,
    private val startupTimeoutMs: Long,
    private var onSuccess: () -> Unit = {},
    private var onFailure: (String) -> Unit = {}
) : DeployStrategy {

    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var projectDir: File
    private lateinit var workdir: File
    private val resources = mutableListOf<Closeable>()
    private val currentProcess = AtomicReference<Process>()

    override fun configure(tfw: TFWFacade) {
        projectDir = File(tfw.tfwConfig.projectDir)
        workdir = File(tfw.tfwConfig.workdir)
        require(projectDir.isDirectory) {
            "projectDir ($projectDir) is not a directory."
        }
        require(workdir.isDirectory) {
            "workdir ($workdir) is not a directory."
        }
        println("Using project directory: ${projectDir.absolutePath}")
        println("Using workdir: ${workdir.absolutePath}")
        resources.add(tfw.onDeployStart {
            deploy(tfw)
            KeepSubscription
        })
    }

    private fun deploy(tfw: TFWFacade) {
        executor.submit {
            val errorText = StringBuilder()
            val stdout = File.createTempFile("tfw_ws_stdout_", ".log")
            val stderr = File.createTempFile("tfw_ws_stderr_", ".log")
            try {
                currentProcess.get()?.let {
                    println("Current process already exists, destroying...: $it")
                    it.destroyForcibly()
                }
                val pb = ProcessBuilder(runCommand.split(" "))
                pb.directory(projectDir)
                pb.redirectInput(stdout)
                pb.redirectError(stderr)
                println("Stdout log can be found at ${stdout.absolutePath}.")
                println("Stderr log can be found at ${stderr.absolutePath}.")
                currentProcess.set(pb.start())
                val startTime = System.currentTimeMillis()
                val maxTime = startTime + startupTimeoutMs
                var success = false
                while (System.currentTimeMillis() < maxTime && success.not()) {
                    Thread.sleep(15000)
                    try {
                        val response: HttpResponse = HttpClientBuilder.create().build()
                            .execute(HttpGet(healthCheckUrl))
                        val statusCode: Int = response.statusLine.statusCode
                        if (statusCode == HttpStatus.SC_OK) {
                            success = true
                        }
                    } catch (e: Exception) {
                        if (currentProcess.get().isAlive.not()) {
                            throw IllegalStateException(
                                "Process exited abnormally: ${
                                    currentProcess.get().exitValue()
                                }"
                            )
                        }
                    }
                }
                if (success) {
                    println("Deploy was successful")
                    tfw.signalDeploySuccess()
                    onSuccess()
                } else {
                    println("Deploy was unsuccessful.")
                    errorText.append(
                        currentProcess.get().errorStream
                            .readBytes().toString(Charset.forName("UTF-8"))
                    )
                    currentProcess.get()?.destroyForcibly()
                    tfw.signalDeployFailure(
                        errorText.toString()
                    )
                    onFailure(errorText.toString())
                }
            } catch (e: Exception) {
                println("Deploy was terminated unsuccessfully.")
                e.printStackTrace()
                currentProcess.get()?.destroyForcibly()
                tfw.signalDeployFailure(e.message ?: errorText.toString())
                onFailure(errorText.toString())
            }
        }
    }

    override fun close() {
        try {
            executor.shutdownNow()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            currentProcess.get()?.destroyForcibly()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        resources.forEach {
            try {
                it.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
