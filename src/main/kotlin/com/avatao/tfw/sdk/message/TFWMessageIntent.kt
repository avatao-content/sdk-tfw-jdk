package com.avatao.tfw.sdk.message

/**
 * A [TFWMessageIntent] can be used to control the behavior of event listeners with a [TFWMessage].
 * It has two valid values: "control" and "event". The former is used to instruct
 * an event handler to perform an action and the latter emits information about something
 * (RPC and event advertisement).
 *
 * This distinction is really important, because the lack of intent could lead to infinite
 * recursion in some event handlers. Generally there is no need to explicitly set this field
 * unless you are creating complex event handlers with several available commands and
 * emitted events (most of the time you don't really have to care this exists).
 *
 * The default value is "control" (messages without an intent key are treated as control messages).
 */
enum class TFWMessageIntent(
    val value: String
) {
    CONTROL("control"),
    EVENT("event");
}