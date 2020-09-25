import org.zeromq.SocketType
import org.zeromq.ZMQ
import sdk.API
import sdk.util.Operations
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


private val context: ZMQ.Context = ZMQ.context(1)
val pullSocket = context.socket(SocketType.PULL)
val pullSocketConnectionString = "tcp://localhost:8765" // + System.getenv("TFW_PULL_PORT") // 8765
private val poller = context.poller(1)

fun tred() {
        val latch = CountDownLatch(1)

    println("tredstartelőtt")
    val thread = Thread {
        while (true) {
            poller.poll()
            println("polly1")
            val message = pullSocket.recvStr(0)
            println("----- $message")
            if (message.contains("key"))
                println(message)
        }
            latch.countDown()
    }
        latch.await(5, TimeUnit.SECONDS)

    println("tredstartután")
}

fun main(){
    val api = API()

    tred()

    api.setReloadSite(true)

//
//    /* INSTANTIATING AN EVENT HANDLER */
//    val cdeh = api.getDeployEventHandler {
//        // CUSTOM DEPLOY LOGIC
//        api.finishDeploy() // a must-have
//    }
//
//    /* INSTANTIATING FSMStepsEventHandler WITH NO ADDITIONAL LOGIC */
//    val fsmdh = api.getFSMStepsEventHandler()
//    // You can get the actual state with api.getActualState() then
//
//    /* INSTANTIATING FSMStepsEventHandler WITH ADDITIONAL LOGIC */
//    val fsmdh2 = api.getFSMStepsEventHandler { message: String ->
//        //val currentState = api.getValueFromJSONMessage(message,"current_state")
//        //println("[FSMStepsEventHandler] - CURRENT STATE: $currentState")
//        println("[FSMStepsEventHandler] - CURRENT STATE: $message")
//    }
//
//    /* INSTANTIATING ConsoleReadEventHandler */
//    val creh = api.getConsoleReadEventHandler { message: String ->
//        val consoleContent = api.getValueFromJSONMessage(message, "content")
//        println("[ConsoleReadEventHandler] - CONSOLE CONTENT: $consoleContent")
//    }
//
//    /* INSTANTIATING HistoryMonitorEventHandler */
//    val hmh = api.getHistoryMonitorEventHandler { message: String ->
//        val lastCommand = api.getValueFromJSONMessage(message, "command")
//        println("[HistoryMonitorEventHandler] - LAST COMMAND: $lastCommand")
//    }
//
//    /* INSTANTIATING TypingEventHandler */
//    val teh = api.getTypingEventHandler { message: String ->
//        var selectedFileContent = api.getValueFromJSONMessage(message, "content")
//
//        val path = "/home/user/workdir/cat.txt"
//        val anotherFileContent = Operations.getFileContent(path)
//    }

    /* SENDING TRIGGERS TO TFW */
//    api.setReloadSite(true)
//    api.setDocumentTitle("new title")
//    api.switchLayout("ide-only")
//    api.setHideMessages(true)
//    api.setIframeURL("new_url")
//    api.setShowUrlBar(false)
//    api.switchToConsole()
//    api.switchToTerminal()
//    api.setShowDeployButton(false)
//    api.setDeployButtonText(listOf("Deploy", "Deployed", "Deploying...", "Failed"))
//    api.reloadIframe()
//    //api.selectFile("/home/user/workdir/cat.txt")
//    //api.writeToFile("/home/user/workdir/cat.txt", "Content overrided")
//    api.reloadIde()
//    api.finishDeploy()
//    api.writeToTerminal("terminal_text")
//    api.writeToConsole("console_text")
//    api.readConsole()
//    api.startProcess("webservice")
//    api.stopProcess("webservice")
//    api.restartProcess("webservice")
//    api.step(1)
//    api.stepTo(2)

    /* SENDING MESSAGES */
//    api.sendMessage("Message 1")
//    api.queueMessages(listOf("MessageQue1", "MessageQue2"))

    /* DESTROYING AN EVENT HANDLER */
    // cdeh.destroy()

    /* CLOSING THE CONTEXT */
    //api.closeAPIContext()
}
