package com.warehouseservice.utils

import com.warehouseservice.domain.Pallet

interface WarehouseState {

    fun getById(palletId: String): Pallet?

    fun getAll(): ArrayList<Pallet>

    fun getLastModified(): String

    fun setLastModified(lastUpdate: String)

    fun removeById(palletId: String)

    fun upsert(pallet: Pallet)

    fun getByProductName(productName: String): List<Pallet>?

    fun getByStorageLocation(location: String): Pallet?

    fun getPalletsNotStores(): List<Pallet>
}