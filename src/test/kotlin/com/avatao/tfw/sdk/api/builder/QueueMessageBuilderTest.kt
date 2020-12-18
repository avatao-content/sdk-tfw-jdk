package com.avatao.tfw.sdk.api.builder

import com.avatao.tfw.sdk.api.MessageAPI
import com.avatao.tfw.sdk.connector.TFWServerConnector
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@DisplayName("Given a queue message builder")
class QueueMessageBuilderTest {

    @Mock
    private lateinit var connectorMock: TFWServerConnector

    @Mock
    private lateinit var messageAPIMock: MessageAPI

    private lateinit var target: QueueMessageBuilder

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        target = QueueMessageBuilder(connectorMock, messageAPIMock)
    }

    @DisplayName("When building a queue with multiple messages Then the proper json should be constructed")
    @Test
    fun shouldConstructProperJson() {
        val result = target
            .message {
                message("Uzi 1")
            }
            .message {
                message("Uzi 2")
                buttons(listOf("yes", "no"))
            }
            .build()

        assertEquals(
            "{\"messages\":[{\"message\":\"Uzi 1\"},{\"message\":\"Uzi 2\",\"buttons\":[\"yes\",\"no\"]}],\"key\":\"message.queue\"}",
            result.rawJson
        )
    }
}


