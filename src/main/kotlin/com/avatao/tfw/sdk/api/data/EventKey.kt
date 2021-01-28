package com.avatao.tfw.sdk.api.data

enum class EventKey(
    val value: String
) {
    FRONTEND_SITE("frontend.site"),
    FRONTEND_DASHBOARD("frontend.dashboard"),
    FRONTEND_IDE("frontend.ide"),
    MESSAGE_SEND("message.send"),
    MESSAGE_QUEUE("message.queue"),
    IDE_READ("ide.read"),
    IDE_WRITE("ide.write"),
    DEPLOY_START("deploy.start"),
    DEPLOY_FINISH("deploy.finish"),
    CONSOLE_WRITE("console.write"),
    TERMINAL_WRITE("terminal.write"),
    HISTORY_BASH("history.bash"),
    PROCESS_LOG_NEW("process.log.new"),
    PROCESS_LOG_SET("process.log.set"),
    FSM_TRIGGER("fsm.trigger"),
    FSM_UPDATE("fsm.update"),
    FSM_CREATE("fsm.create"),
    BUTTON_CLICK("message.button.click"),
    RELOAD_IFRAME("dashboard.reloadIframe")
}
