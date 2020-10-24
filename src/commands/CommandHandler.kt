package com.warehouseservice.commands

import com.warehouseservice.commands.palletCommands.CreatePalletCommand
import com.warehouseservice.commands.palletCommands.DeletePalletCommand
import com.warehouseservice.commands.palletCommands.UpdatePalletCommand
import com.warehouseservice.events.Event
import com.warehouseservice.events.Subscriber

interface CommandHandler: Subscriber {

    override suspend fun handleCommand(command: Command)

    override suspend fun handle(event: Event)

    suspend fun addPallet(createPalletCommand: CreatePalletCommand)

    suspend fun updatePallet(updatePalletCommand: UpdatePalletCommand)

    suspend fun deletePallet(deletePalletCommand: DeletePalletCommand)
}