/*
 * Copyright (C) 2020 Avatao.com Innovative Learning Kft.
 * All Rights Reserved. See LICENSE file for details.
 */
package sdk.eventhandling.customhandlers

import sdk.API
import sdk.eventhandling.EventHandler

class DeployEventHandler(api: API, handleEvent: (String) -> Unit) :
        EventHandler("deploy.start", api, handleEvent)