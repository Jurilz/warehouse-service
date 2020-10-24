package com.warehouseservice.events

import com.warehouseservice.commands.Command

interface Subscriber {

    suspend fun handle(event: Event)

    suspend fun handleCommand(command: Command)
}