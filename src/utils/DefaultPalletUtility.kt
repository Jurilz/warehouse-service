package com.warehouseservice.utils

import com.warehouseservice.domain.Pallet
import com.warehouseservice.events.Event
import com.warehouseservice.events.palletEvents.*
import com.warehouseservice.repository.EventWriteRepository

class DefaultPalletUtility(private val eventWriteRepository: EventWriteRepository,
                           private val state: WarehouseState)
    : PalletUtility {

    override fun getById(palletId: String): Pallet? {
        return state.getById(palletId)
    }

    override suspend fun recreatePalletState(): ArrayList<Pallet> {
        val palletEvents: List<Event> = eventWriteRepository.getAll()
        for (event: Event in palletEvents) {
            when(event) {
                is PalletCreatedEvent -> addPalletToState(event)
                is PalletUpdatedEvent -> updatePalletState(event)
                is PalletDeletedEvent -> deletePalletFromPallets(event)
            }
        }
        return state.getAll()
    }

    override fun getByProductName(productName: String): List<Pallet>? {
        return state.getByProductName(productName)
    }

    override fun getByStorageLocation(location: String): Pallet? {
        return state.getByStorageLocation(location)
    }

    override fun deletePalletFromPallets(event: PalletDeletedEvent) {
        if (event.lastModified > state.getLastModified()) {
            deleteFromPallets(event)
            state.setLastModified(event.lastModified)
        }
    }

    private fun deleteFromPallets(event: PalletDeletedEvent) {
        val pallet: Pallet? = state.getById(event.palletEvent.palletId)
        if (pallet != null) {
            this.state.removeById(event.palletEvent.palletId)
        }
    }

    private fun addPalletToState(event: PalletCreatedEvent) {
        if (event.lastModified > state.getLastModified()) {
            addToPallets(event)
            state.setLastModified(event.lastModified)
        }
    }

    private fun addToPallets(event: PalletCreatedEvent) {
        val addPallet: Pallet = event.palletEvent.buildPallet()
        state.upsert(addPallet)
    }


    private fun updatePalletState(event: PalletUpdatedEvent) {
        if (event.lastModified > state.getLastModified()) {
            updatePallets(event)
            state.setLastModified(event.lastModified)
        }
    }

    private fun updatePallets(event: PalletUpdatedEvent) {
        val updatePallet: Pallet = event.palletEvent.buildPallet()
        state.upsert(updatePallet)
    }

    override fun getPalletsNotStores(): List<Pallet> {
        return state.getPalletsNotStores()
    }
}