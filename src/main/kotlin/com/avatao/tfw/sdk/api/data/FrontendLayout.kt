package com.avatao.tfw.sdk.api.data

enum class FrontendLayout(
    val value: String
) {
    TERMINAL_IDE_WEB("terminal-ide-web"),
    TERMINAL_IDE_VERTICAL("terminal-ide-vertical"),
    TERMINAL_WEB("terminal-web"),
    IDE_WEB_VERTICAL("ide-web-vertical"),
    TERMINAL_IDE_HORIZONTAL("terminal-ide-horizontal"),
    TERMINAL_ONLY("terminal-only"),
    IDE_ONLY("ide-only"),
    WEB_ONLY("web-only")
}