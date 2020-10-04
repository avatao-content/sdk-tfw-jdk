package com.avatao.tfw.sdk.mock

import org.zeromq.SocketType
import org.zeromq.ZMQ
import org.zeromq.ZMsg
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class TFWServerMock : Closeable {

    private val context: ZMQ.Context = ZMQ.context(1)
    private var running = AtomicBoolean(false)

    // TODO: env var names good?
    private val pullHost = System.getenv("TFW_PULL_HOST") ?: "localhost"
    private val pullPort = System.getenv("TFW_PULL_PORT") ?: "8765"
    private val receiver = context.socket(SocketType.PULL)

    private val pubHost = System.getenv("TFW_PUB_HOST") ?: "localhost"
    private val pubPort = System.getenv("TFW_PUB_PORT") ?: "8766"
    private val publisher = context.socket(SocketType.PUB)

    init {
        publisher.bind("tcp://$pubHost:$pubPort")
        receiver.connect("tcp://$pullHost:$pullPort")
        val items: ZMQ.Poller = context.poller()
        items.register(receiver, ZMQ.Poller.POLLIN)
        running.set(true)
        thread {
            while (running.get()) {
                println("Polling for items...")
                items.poll()
                if (items.pollin(0)) {
                    ZMsg.recvMsg(receiver, false)?.let { msg ->
                        println("Received msg: ${msg.toArray().joinToString()}")
                    }
                }
            }
        }
    }

    override fun close() {
        running.set(false)
        publisher.close()
        receiver.close()
        context.term()
    }

    companion object {

        @JvmStatic
        fun create() = TFWServerMock()
    }
}