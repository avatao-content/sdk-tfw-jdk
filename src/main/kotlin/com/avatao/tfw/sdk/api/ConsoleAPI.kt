package com.avatao.tfw.sdk.api

interface ConsoleAPI {

    fun readConsole(): String

    fun writeToConsole(content: String)

    /**
     * Retrieves the console's content.
     * You have to have an event handler subscribed with
     * the "console.read" key, too.
     */
    // TODO: so how does this supposed to work?
//    fun readConsole(){
//        sendToTFW("console.read")
//    }

}