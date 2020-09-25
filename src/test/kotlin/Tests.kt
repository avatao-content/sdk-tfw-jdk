import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.zeromq.SocketType
import org.zeromq.ZMQ
import sdk.API

class Tests {
    private val context: ZMQ.Context = ZMQ.context(1)
    private val pullSocket = context.socket(SocketType.PULL)
    private val pullSocketConnectionString = "tcp://localhost:8765" // + System.getenv("TFW_PULL_PORT") // 8765
    private val poller = context.poller(1)
    private val api = API()

    @BeforeEach
    fun connect(){
        pullSocket.connect(pullSocketConnectionString)
        pullSocket.bind(pullSocketConnectionString)
        poller.poll()
    }

    @AfterEach
    fun close(){
        pullSocket.unbind(pullSocketConnectionString)
        pullSocket.close()
        poller.close()
    }

    private fun startPolling() : String{
        while (true) {
            val message = pullSocket.recvStr(0)
            if (message.contains("key")) {
                return message
            }
        }
    }

    @Test
    fun setReloadSiteTest() {
        val expected = "{\"key\":\"frontend.site\",\"askReloadSite\":\"true\"}"
        var actual = ""
        GlobalScope.launch {
            actual = startPolling()
        }
        api.setReloadSite(true)
        Thread.sleep(1000L)
        assertEquals(expected, actual)
    }

    @Test
    fun setReloadSiteTest2() {
        val expected = "{\"key\":\"frontend.site\",\"askReloadSite\":\"true\"}"
        var actual = ""
        GlobalScope.launch {
            actual = startPolling()
        }
        api.setReloadSite(true)
        Thread.sleep(1000L)
        assertEquals(expected, actual)
    }
}
