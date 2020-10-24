package com.warehouseservice.events.palletEvents

import com.warehouseservice.domain.Pallet
import com.warehouseservice.events.Event


class PalletEvent(
    val palletId: String,
    val product: String,
    val amount: Int,
    val priority: String,
    val userId: String,
    val storageLocation: String?,
    val state: String
): Event()

fun PalletEvent.buildPallet(): Pallet {
    return Pallet(
        palletId = this.palletId,
        product = this.product,
        amount = this.amount,
        priority = this.priority,
        userId = this.userId,
        storageLocation = this.storageLocation,
        state = this.state,
        lastModified = this.lastModified
    )
}