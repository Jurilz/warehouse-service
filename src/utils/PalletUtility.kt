package com.warehouseservice.utils

import com.warehouseservice.domain.Pallet
import com.warehouseservice.events.palletEvents.PalletDeletedEvent


interface PalletUtility {

    fun getById(palletId: String): Pallet?

    suspend fun recreatePalletState(): ArrayList<Pallet>

    fun getByProductName(productName: String): List<Pallet>?

    fun getByStorageLocation(location: String): Pallet?

    fun deletePalletFromPallets(event: PalletDeletedEvent)

    fun getPalletsNotStores(): List<Pallet>
}