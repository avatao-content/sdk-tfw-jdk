package com.avatao.tfw.sdk.message

class TFWMessageBuilder {

    private var key: String? = null
    private var scope: TFWMessageScope = TFWMessageScope.ZMQ
    private var intent: TFWMessageIntent = TFWMessageIntent.CONTROL
    private val rest = mutableMapOf<String, Any>()

    fun withKey(key: String) = also {
        this.key = key
    }

    fun withScope(scope: TFWMessageScope) = also {
        this.scope = scope
    }

    fun withIntent(intent: TFWMessageIntent) = also {
        this.intent = intent
    }

    fun withValue(key: String, value: Any) = also {
        rest[key] = value
    }

    fun withValues(values: Map<String, Any>) = also {
        rest.putAll(values)
    }

    fun build(): TFWMessage {
        require(key !== null) {
            "Can't build a TFWMessage without a key."
        }
        return DefaultTFWMessage(
            key = key!!,
            intent = intent,
            scope = scope,
            data = rest.toMap() + mapOf(
                "key" to key!!,
                "intent" to intent.value,
                "scope" to scope.value
            )
        )
    }


    class DefaultTFWMessage(
        override val key: String,
        override val intent: TFWMessageIntent,
        override val scope: TFWMessageScope,
        override val data: Map<String, Any>
    ) : TFWMessage
}