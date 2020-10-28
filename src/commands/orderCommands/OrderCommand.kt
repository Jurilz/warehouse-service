package com.warehouseservice.commands.orderCommands

import com.warehouseservice.commands.Command
import com.warehouseservice.events.orderEvents.OrderEvent

open class OrderCommand(
    val orderId: String,
    val productName: String,
    val amount: Int,
    val customerName: String,
    val address: String,
    val lastModified: String,
    val state: String
): Command()

fun OrderCommand.buildOrderEvent(state: String): OrderEvent {
    val orderEvent = OrderEvent(
        orderId = this.orderId,
        productName = this.productName,
        customerName = this.customerName,
        amount = this.amount,
        address = this.address,
        state = state
    )
    orderEvent.lastModified = this.lastModified
    return orderEvent
}