package com.warehouseservice.commands

import com.warehouseservice.commands.orderCommands.CancelOrderCommand
import com.warehouseservice.commands.orderCommands.OrderCommand
import com.warehouseservice.commands.orderCommands.ValidateOrderCommand
import com.warehouseservice.commands.orderCommands.buildOrderEvent
import com.warehouseservice.commands.palletCommands.*
import com.warehouseservice.domain.OrderState
import com.warehouseservice.domain.PalletState
import com.warehouseservice.events.Event
import com.warehouseservice.events.MessageTopic
import com.warehouseservice.events.MessageBroker
import com.warehouseservice.events.orderEvents.OrderEvent
import com.warehouseservice.events.orderEvents.OrderUpdatedEvent
import com.warehouseservice.events.palletEvents.PalletCreatedEvent
import com.warehouseservice.events.palletEvents.PalletDeletedEvent
import com.warehouseservice.events.palletEvents.PalletEvent
import com.warehouseservice.events.palletEvents.PalletUpdatedEvent

class DefaultCommandHandler(private val messageBroker: MessageBroker): CommandHandler {

    override suspend fun handleCommand(command: Command) {
        when(command) {
            is CreatePalletCommand -> addPallet(command)
            is UpdatePalletCommand -> updatePallet(command)
            is DeletePalletCommand -> deletePallet(command)
            is ValidateOrderCommand -> validateOrder(command)
            is CancelOrderCommand -> cancelOrder(command)
        }
    }

    private suspend fun cancelOrder(command: CancelOrderCommand) {
        val newState: String = determineOrderState(command.orderCommand)
        val cancelEvent: OrderEvent = command.orderCommand.buildOrderEvent(newState)
        val orderCanceledEvent = OrderUpdatedEvent(cancelEvent)
        messageBroker.publish(MessageTopic.PUBLISHER, orderCanceledEvent)
    }

    private suspend fun validateOrder(command: ValidateOrderCommand) {
        messageBroker.publishCommand(MessageTopic.PRODUCT_SERVICE, command)
    }

    override suspend fun addPallet(createPalletCommand: CreatePalletCommand) {
        val newState: String = determinePalletState(createPalletCommand.palletCommand)
        val palletEvent: PalletEvent = createPalletCommand.palletCommand.buildPalletEvent(newState)
        val palletCreatedEvent = PalletCreatedEvent(palletEvent)
        messageBroker.publish(MessageTopic.PALLET_SERVICE, palletCreatedEvent)
    }

    override suspend fun updatePallet(updatePalletCommand: UpdatePalletCommand) {
        val newState: String = determinePalletState(updatePalletCommand.palletCommand)
        val palletEvent: PalletEvent = updatePalletCommand.palletCommand.buildPalletEvent(newState)
        val palletUpdatedEvent = PalletUpdatedEvent(palletEvent)
        messageBroker.publish(MessageTopic.PALLET_SERVICE, palletUpdatedEvent)
    }

    override suspend fun deletePallet(deletePalletCommand: DeletePalletCommand) {
        val newState: String = determinePalletState(deletePalletCommand.palletCommand)
        val palletEvent: PalletEvent = deletePalletCommand.palletCommand.buildPalletEvent(newState)
        val palletDeletedEvent = PalletDeletedEvent(palletEvent)
        messageBroker.publish(MessageTopic.PALLET_SERVICE, palletDeletedEvent)
    }

    private fun determinePalletState(palletCommand: PalletCommand): String {
        return when (palletCommand.state) {
            PalletState.addedInFrontend.toString() -> PalletState.addedInBackend.toString()
            PalletState.storedInFrontend.toString() -> PalletState.storedInBackend.toString()
            else -> palletCommand.state
        }
    }

    private fun determineOrderState(orderCommand: OrderCommand): String {
        return when(orderCommand.state) {
            OrderState.cancelationProccedByOrderService.toString() -> OrderState.cancelationProccedByWarehouse.toString()
            OrderState.beingDelivered.toString() -> OrderState.canceledInDelivery.toString()
            else -> orderCommand.state
        }
    }

    override suspend fun handle(event: Event) {
        TODO("Not yet implemented")
    }
}