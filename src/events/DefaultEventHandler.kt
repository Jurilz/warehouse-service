package com.warehouseservice.events

import com.warehouseservice.commands.Command
import com.warehouseservice.domain.OrderState
import com.warehouseservice.domain.PalletState
import com.warehouseservice.events.orderEvents.OrderEvent
import com.warehouseservice.events.orderEvents.OrderUpdatedEvent
import com.warehouseservice.events.orderEvents.buildOrderEvent
import com.warehouseservice.events.palletEvents.PalletCreatedEvent
import com.warehouseservice.events.palletEvents.PalletDeletedEvent
import com.warehouseservice.events.palletEvents.PalletUpdatedEvent
import com.warehouseservice.projectors.ProductProjector


class DefaultEventHandler(
    private val projector: ProductProjector,
    private val messageBroker: MessageBroker
): EventHandler {

    override suspend fun handle(event: Event) {
        when(event) {
            is PalletUpdatedEvent -> handlePalletUpdated(event)
            is PalletDeletedEvent -> projector.removeProducts(event)
            is OrderUpdatedEvent -> handleOrderUpdate(event)
        }
    }

    private suspend fun handleOrderUpdate(event: OrderUpdatedEvent) {
        when(event.orderEvent.state) {
            OrderState.cancelationProccedByOrderService.toString() -> approveCancellation(event)
        }
    }

    private suspend fun handlePalletUpdated(event: PalletUpdatedEvent) {
        if (event.palletEvent.state == PalletState.storedInBackend.toString()) {
            projector.upsertProducts(event)
        }
    }


    private suspend fun approveCancellation(event: OrderUpdatedEvent) {
        val newState: String = determineNewState(event.orderEvent)
        val cancelEvent: OrderEvent = event.orderEvent.buildOrderEvent(newState)
        val orderCanceledEvent = OrderUpdatedEvent(cancelEvent)
        messageBroker.publish(MessageTopic.PUBLISHER, orderCanceledEvent)
    }

    private fun determineNewState(event: OrderEvent): String {
        return when(event.state) {
            OrderState.cancelationProccedByOrderService.toString() -> OrderState.cancelationProccedByWarehouse.toString()
            else -> event.state
        }
    }

    override suspend fun handleCommand(command: Command) {
        TODO("Not yet implemented")
    }


}
