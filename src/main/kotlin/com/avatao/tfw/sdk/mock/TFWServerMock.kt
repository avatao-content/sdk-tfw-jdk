package com.avatao.tfw.sdk.mock

import com.avatao.tfw.sdk.api.data.CurrentFSMState
import com.avatao.tfw.sdk.api.data.EventKey
import com.avatao.tfw.sdk.api.data.EventKey.*
import com.avatao.tfw.sdk.api.data.StateTransition
import com.avatao.tfw.sdk.api.data.ValidTransition
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.zeromq.SocketType
import org.zeromq.ZFrame
import org.zeromq.ZMQ
import org.zeromq.ZMsg
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread

@Suppress("UNCHECKED_CAST")
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
    private val objectMapper = ObjectMapper()

    private val states = mutableListOf<String>()
    private val currentState = AtomicReference<String>()
    private var lastState = AtomicBoolean(false)

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
                        val key = msg.popString()
                        val value = msg.popString()
                        val data: Map<String, Any> =
                            objectMapper.readValue(value, object : TypeReference<Map<String, Any>>() {})
                        println("Received message: $key -> $data")
                        when (key) {
                            DEPLOY_START.value -> publisher.send(key, value)
                            FSM_CREATE.value -> {
                                states.addAll(data["states"] as List<String>)
                                currentState.set(states.first())
                            }
                            FSM_TRIGGER.value -> {
                                triggerUpdate(data)
                            }
                            else -> {
                                println("Message ignored: $key -> $value")
                            }
                        }
                    }

                }
            }
        }
    }

    private fun triggerUpdate(value: Map<String, Any>) {
        val prevState = currentState.get()
        currentState.set(value["transition"] as String)
        val stateIdx = states.indexOf(currentState.get())
        val validTransitions = if (stateIdx < states.size) {
            listOf(ValidTransition(states[stateIdx + 1]))
        } else listOf()
        publisher.send(
            key = FSM_UPDATE.value,
            value = Json.encodeToString(CurrentFSMState(
                currentState = currentState.get(),
                inAcceptedState = lastState.get(),
                lastEvent = StateTransition(
                    fromState = prevState,
                    toState = currentState.get(),
                    trigger = currentState.get(),
                    timestamp = ""
                ),
                validTransitions = validTransitions
            ))
        )
    }

    private fun ZMQ.Socket.send(key: String, value: String) {
        val socket = this
        ZMsg().apply {
            add(ZFrame(key))
            add(ZFrame(value))
            send(socket)
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
