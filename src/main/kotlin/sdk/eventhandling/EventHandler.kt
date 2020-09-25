/*
 * Copyright (C) 2020 Avatao.com Innovative Learning Kft.
 * All Rights Reserved. See LICENSE file for details.
 */
package sdk.eventhandling

import org.zeromq.SocketType
import org.zeromq.ZMQ
import sdk.API

abstract class EventHandler(
        private val key: String,
        api: API,
        private val handleEvent: (String) -> Unit
){
    private val context = api.getConnector().context
    private val pullSocket = context.socket(SocketType.SUB)
    private val pullSocketUrl = "tcp://localhost:7654"// + System.getenv("TFW_PUB_PORT") // 7654
    private val poller = context.poller(1)

    init {
        println("Event handler started with key \"$key\"")
        connect()
        subscribe()
        registerPoller()
        start()
    }

    /** Connect to TFWServer. */
    private fun connect() {
        pullSocket.connect(pullSocketUrl)
    }

    /** Subscribe a socket for polling */
    private fun subscribe() {
        pullSocket.subscribe(key.toByteArray(Charsets.UTF_8))
    }

    /** Register to a given key */
    private fun registerPoller() {
        poller.register(pullSocket, ZMQ.Poller.POLLIN)
    }

    /** Close connection with TFWServer. */
    fun destroy() {
        poller.close()
        poller.unregister(pullSocket)
        pullSocket.close()
    }

    private fun start(){
        val thread = Thread {
            while (true) {
                poller.poll()
                val message = pullSocket.recvStr(0)
                if (message.contains("key"))
                    handleEvent.invoke(message)
            }
        }
        thread.start()
    }
}
