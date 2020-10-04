package com.avatao.tfw.sdk

import com.avatao.tfw.sdk.api.data.FileContents
import com.avatao.tfw.sdk.api.data.FileWriteResults
import com.avatao.tfw.sdk.api.data.KeepSubscription
import com.avatao.tfw.sdk.api.data.ProcessLogEntry
import com.avatao.tfw.sdk.message.TFWMessage
import com.avatao.tfw.sdk.message.TFWMessageScope
import com.avatao.tfw.sdk.mock.TFWServerMock
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


fun main() {

    val mock = TFWServerMock()
    val api = TFW.create()
    api.frontend {
        onFrontendReady {
            configureDashboard().hideMessages(true).commit()
            configureIDE().autoSaveInterval(1).commit()
            configureSite().askReloadSite(true).commit()
        }
    }.webIDE {
        onReadFile { command ->
            command.respondWith(
                FileContents(
                    filename = command.filename,
                    content = "content",
                    files = listOf()
                )
            )
            KeepSubscription
        }

        onWriteFile { command ->
            command.respondWith(
                FileWriteResults(
                    filename = command.filename,
                    files = listOf()
                )
            )
            KeepSubscription
        }

        onDeployStart {
            signalDeploySuccess()
            KeepSubscription
        }
    }.connector {
        send(
            TFWMessage.builder()
                .withKey("frontend.site")
                .withScope(TFWMessageScope.BROADCAST)
                .withValue("documentTitle", "some title")
                .build()
        )
    }.process {

        try {
            val startResult = startProcess("webservice")[1L, TimeUnit.SECONDS]
            println(startResult)

            val restartResult = restartProcess("webservice")[1L, TimeUnit.SECONDS]
            println(restartResult)

            val stopResult = stopProcess("webservice")[1L, TimeUnit.SECONDS]
            println(stopResult)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        configLog()
            .name("webservice")
            .tail(100)
            .commit()

        readLog().subscribe(object : Subscriber<ProcessLogEntry> {

            private lateinit var subscription: Subscription
            private val counter = AtomicInteger()

            override fun onSubscribe(s: Subscription?) {
                subscription = s!!
                subscription.request(Long.MAX_VALUE)
            }

            override fun onNext(t: ProcessLogEntry?) {
                println(t)
                counter.incrementAndGet()
                if (counter.get() > 10) {
                    subscription.cancel()
                }
            }

            override fun onError(t: Throwable?) {
                t?.printStackTrace()
            }

            override fun onComplete() {
                println("Completed")
            }
        })
    }

    api.sendMessage().message("hey.").commit()
    api.writeToConsole("Hello, Console!")

    api.close()
    mock.close()
}