package com.warehouseservice.commands.palletCommands

import com.warehouseservice.commands.Command
import com.warehouseservice.events.palletEvents.PalletEvent

open class PalletCommand (
    val palletId: String,
    val product: String,
    val amount: Int,
    val priority: String,
    val userId: String,
    val storageLocation: String?,
    val state: String,
    val lastModified: String
): Command() {

    override fun execute() {
        TODO("Not yet implemented")
    }
}

fun PalletCommand.buildPalletEvent(state: String): PalletEvent {
    val palletEvent = PalletEvent(
        palletId = this.palletId,
        product = this.product,
        amount = this.amount,
        priority = this.priority,
        userId = this.userId,
        storageLocation = this.storageLocation,
        state = state
    )
    palletEvent.lastModified = this.lastModified
    return palletEvent
}