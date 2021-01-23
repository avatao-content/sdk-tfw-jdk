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
        val data = rest.toMutableMap()
        data["key"] = key!!
        if (intent != TFWMessage.DEFAULT_INTENT) {
            data["intent"] = intent.value
        }
        if (scope != TFWMessage.DEFAULT_SCOPE) {
            data["scope"] = scope.value
        }
        return DefaultTFWMessage(
            key = key!!,
            intent = intent,
            scope = scope,
            data = data
        )
    }

    class DefaultTFWMessage(
        override val key: String,
        override val intent: TFWMessageIntent,
        override val scope: TFWMessageScope,
        override val data: Map<String, Any>
    ) : TFWMessage
}
