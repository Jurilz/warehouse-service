package com.warehouseservice.services

import com.warehouseservice.domain.Pallet
import com.warehouseservice.events.Event
import com.warehouseservice.events.Subscriber

interface PalletService: Subscriber {

    override suspend fun handle(event: Event)

    suspend fun initializeService()

    suspend fun storeAndPublishPalletEvent(event: Event)

    suspend fun getAll(): List<Pallet>

    suspend fun getById(palletId: String): Pallet?

    suspend fun getByProductName(productName: String): List<Pallet>?

    suspend fun getByStorageLocation(location: String): Pallet?

    suspend fun getPallets(): List<Pallet>

}