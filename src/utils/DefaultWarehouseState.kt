package com.warehouseservice.utils

import com.warehouseservice.domain.Pallet
import com.warehouseservice.domain.PalletState
import com.warehouseservice.domain.Product


class DefaultWarehouseState: WarehouseState {
    val pallets: HashMap<String, Pallet> = HashMap()
    val products: HashMap<String, Product> = HashMap()

    var lastUpdate: String = ""

    override fun getById(palletId: String): Pallet? {
        return pallets[palletId]
    }

    override fun getAll(): ArrayList<Pallet> {
        return ArrayList(pallets.values)
    }

    override fun getLastModified(): String {
        return lastUpdate
    }

    override fun setLastModified(lastUpdate: String) {
        this.lastUpdate = lastUpdate
    }

    override fun removeById(palletId: String) {
        this.pallets.remove(palletId)
    }

    override fun upsert(pallet: Pallet) {
        this.pallets[pallet.palletId] = pallet
    }

    override fun getByProductName(productName: String): List<Pallet>? {
        val pallets = mutableListOf<Pallet>()
        for (pallet: Pallet in this.pallets.values) {
            if (pallet.product == productName) {
                pallets.add(pallet)
            }
        }
        return pallets
    }

    //TODO: return null ?
    override fun getByStorageLocation(location: String): Pallet? {
        for (pallet: Pallet in this.pallets.values) {
            if (pallet.storageLocation  == location) {
                return pallet
            }
        }
        return null
    }

    override fun getPalletsNotStores(): List<Pallet> {
        val pallets: MutableList<Pallet> = mutableListOf()
        for (pallet: Pallet in this.pallets.values) {
            if (pallet.state != PalletState.storedInBackend.toString()) {
                pallets.add(pallet)
            }
        }
        return pallets
    }
}