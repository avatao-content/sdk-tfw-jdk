package com.avatao.tfw.sdk.connector

import com.avatao.tfw.sdk.api.data.CancelSubscription
import com.avatao.tfw.sdk.api.data.SubscriptionCommand
import com.avatao.tfw.sdk.message.TFWMessage
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.zeromq.SocketType
import org.zeromq.ZFrame
import org.zeromq.ZMQ
import org.zeromq.ZMsg
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread


class ZMQServerConnector : TFWServerConnector {

    private val objectMapper: ObjectMapper = ObjectMapper()
    private val context: ZMQ.Context = ZMQ.context(1)
    private var running = AtomicBoolean(true)

    // TODO: env var names good?
    private val pushHost = System.getenv("TFW_PULL_HOST") ?: "localhost"
    private val pushPort = System.getenv("TFW_PULL_PORT") ?: "8765"
    private val publisher = context.socket(SocketType.PUSH)

    private val subHost = System.getenv("TFW_PUB_HOST") ?: "localhost"
    private val subPort = System.getenv("TFW_PUB_PORT") ?: "8766"
    private val subscriber = context.socket(SocketType.SUB)

    private val subscriptions = ConcurrentHashMap<String, CopyOnWriteArrayList<CallbackSubscription>>()

    init {
        publisher.bind("tcp://$pushHost:$pushPort")
        subscriber.connect("tcp://$subHost:$subPort")
        subscriber.subscribe(ZMQ.SUBSCRIPTION_ALL)
        thread {
            while (running.get()) {
                ZMsg.recvMsg(subscriber, false)?.let { msg ->
                    val key = msg.popString()
                    val value: Map<String, Any> =
                        objectMapper.readValue(msg.popString(), object : TypeReference<Map<String, Any>>() {})
                    subscriptions[key]?.map { subscription ->
                        subscription to subscription.fn(TFWMessage.fromMap(value))
                    }?.filter { it.second is CancelSubscription }
                        ?.forEach { (sub) -> sub.close() }
                }
            }
        }
    }

    override fun send(message: TFWMessage) {
        require(running.get()) {
            "This connector is not running anymore."
        }
        ZMsg().apply {
            add(ZFrame(message.key))
            add(ZFrame(objectMapper.writeValueAsString(message.data)))
            send(publisher)
        }
    }

    override fun subscribe(key: String, fn: (TFWMessage) -> SubscriptionCommand): Subscription {
        require(running.get()) {
            "This connector is not running anymore."
        }
        return CallbackSubscription(
            key = key,
            fn = fn
        ).apply {
            subscriptions.getOrPut(key) { CopyOnWriteArrayList() }.add(this)
        }
    }

    override fun close() {
        running.set(false)
        publisher.close()
        subscriber.close()
        context.term()
    }

    private inner class CallbackSubscription(
        override val key: String,
        val fn: (TFWMessage) -> SubscriptionCommand
    ) : Subscription {

        override fun close() {
            subscriptions[key]?.let {
                it.remove(this)
                if (it.isEmpty()) {
                    subscriptions.remove(key)
                }
            }
        }

    }
}