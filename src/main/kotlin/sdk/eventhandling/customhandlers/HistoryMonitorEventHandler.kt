/*
 * Copyright (C) 2020 Avatao.com Innovative Learning Kft.
 * All Rights Reserved. See LICENSE file for details.
 */
package sdk.eventhandling.customhandlers

import sdk.API
import sdk.eventhandling.EventHandler

open class HistoryMonitorEventHandler(api: API, handleEvent: (String) -> Unit) :
        EventHandler("history.bash", api, handleEvent)