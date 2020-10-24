package com.warehouseservice.services

import com.warehouseservice.commands.Command
import com.warehouseservice.domain.Pallet
import com.warehouseservice.events.Event
import com.warehouseservice.events.MessageTopic
import com.warehouseservice.events.MessageBroker
import com.warehouseservice.events.palletEvents.PalletCreatedEvent
import com.warehouseservice.events.palletEvents.PalletDeletedEvent
import com.warehouseservice.events.palletEvents.PalletEvent
import com.warehouseservice.events.palletEvents.PalletUpdatedEvent
import com.warehouseservice.repository.EventWriteRepository
import com.warehouseservice.utils.PalletUtility


class DefaultPalletService(
    private val eventWriteRepository: EventWriteRepository,
    private val palletUtility: PalletUtility,
    private val messageBroker: MessageBroker
): PalletService {

    override suspend fun handle(event: Event) {
        when(event) {
            is PalletCreatedEvent -> storeAndPublishPalletEvent(event)
            is PalletUpdatedEvent -> handlePalletUpdatedEvent(event)
            is PalletDeletedEvent -> storeAndPublishPalletEvent(event)
        }
    }

    private suspend fun handlePalletUpdatedEvent(event: PalletUpdatedEvent) {
        val updatedEvent: PalletUpdatedEvent = validateUpdate(event)
        storeAndPublishPalletEvent(updatedEvent)
    }

    private fun validateUpdate(event: PalletUpdatedEvent): PalletUpdatedEvent {
        return if (event.palletEvent.amount < 0) {
            val updatedEvent = PalletEvent(
                palletId = event.palletEvent.palletId,
                product = event.palletEvent.product,
                amount = 0,
                priority = event.palletEvent.priority,
                userId = event.palletEvent.userId,
                storageLocation = event.palletEvent.storageLocation,
                state = event.palletEvent.state)
            PalletUpdatedEvent(updatedEvent)
        } else {
            event
        }
    }

    override suspend fun initializeService() {
        palletUtility.recreatePalletState()
    }

    override suspend fun storeAndPublishPalletEvent(event: Event) {
        eventWriteRepository.insert(event)
        messageBroker.publish(MessageTopic.PUBLISHER, event)
    }

    override suspend fun getAll(): List<Pallet> {
        return palletUtility.recreatePalletState()
    }


    override suspend fun getPallets(): List<Pallet> {
        palletUtility.recreatePalletState()
        return palletUtility.getPalletsNotStores()
    }

    override suspend fun getById(palletId: String): Pallet? {
        return palletUtility.getById(palletId)
    }

    override suspend fun getByProductName(productName: String): List<Pallet>? {
        return palletUtility.getByProductName(productName)
    }

    override suspend fun getByStorageLocation(location: String): Pallet? {
        return palletUtility.getByStorageLocation(location)
    }

    override suspend fun handleCommand(command: Command) {
        TODO("Not yet implemented")
    }
}