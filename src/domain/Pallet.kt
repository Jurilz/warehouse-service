package com.warehouseservice.domain

import com.warehouseservice.commands.palletCommands.PalletCommand

data class Pallet(
    val palletId: String,
    val product: String,
    val amount: Int,
    val priority: String,
    val userId: String,
    val storageLocation: String?,
    val state: String,
    val lastModified: String
)

fun Pallet.buildPalletCommand(): PalletCommand {
    return PalletCommand(
        palletId = this.palletId,
        product = this.product,
        amount = this.amount,
        priority = this.priority,
        storageLocation = this.storageLocation,
        userId = this.userId,
        state = this.state,
        lastModified = this.lastModified
    )
}