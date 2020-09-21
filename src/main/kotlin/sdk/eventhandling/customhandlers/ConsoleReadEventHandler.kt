/*
 * Copyright (C) 2020 Avatao.com Innovative Learning Kft.
 * All Rights Reserved. See LICENSE file for details.
 */
package sdk.eventhandling.customhandlers

import sdk.API
import sdk.eventhandling.EventHandler

open class ConsoleReadEventHandler(api: API, handleEvent: (String) -> Unit) :
        EventHandler("console.read", api, handleEvent)