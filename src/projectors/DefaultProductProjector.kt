package com.warehouseservice.projectors

import com.warehouseservice.domain.Pallet
import com.warehouseservice.domain.Product
import com.warehouseservice.events.palletEvents.PalletDeletedEvent
import com.warehouseservice.events.palletEvents.PalletUpdatedEvent
import com.warehouseservice.repository.ProductReadRepository
import com.warehouseservice.utils.Util
import com.warehouseservice.utils.PalletUtility

class DefaultProductProjector(
    private val productReadRepository: ProductReadRepository,
    private val palletUtility: PalletUtility
): ProductProjector {

//    override suspend fun project(events: List<Event>) {
//        for (event: Event in events){
//            when(event) {
//                is PalletCreatedEvent -> addProducts(event)
//            }
//        }
//    }

    override suspend fun addProducts(palletUpdatedEvent: PalletUpdatedEvent) {
        val newProduct = Product(palletUpdatedEvent.palletEvent.product, palletUpdatedEvent.palletEvent.amount, Util.getCurrentDate())
        productReadRepository.addOrUpdate(newProduct)
    }

    override suspend fun removeProducts(palletDeletedEvent: PalletDeletedEvent) {
        val removed = Product(palletDeletedEvent.palletEvent.product, palletDeletedEvent.palletEvent.amount, Util.getCurrentDate())
        productReadRepository.removeOrUpdate(removed)
    }

    override suspend fun upsertProducts(event: PalletUpdatedEvent) {
        val product: Product? = productReadRepository.getByName(event.palletEvent.product)
        if (product == null) {
            addProducts(event)
            return
        }
        if (event.lastModified > product.lastModified) {
            val updateProduct: Product = updateProductAmount(event)
            if (updateProduct.items > 0) {
                productReadRepository.updateProduct(updateProduct)
            } else {
                productReadRepository.delete(updateProduct)
            }
        }
    }

    private fun updateProductAmount(event: PalletUpdatedEvent): Product {
        var amount = 0;
        var lastModified = ""
        val pallets: List<Pallet> = palletUtility.getByProductName(event.palletEvent.product)!!
        for (pallet: Pallet in pallets) {
            if (pallet.palletId == event.palletEvent.palletId) {
                amount += event.palletEvent.amount
                if (pallet.lastModified > lastModified) {
                    lastModified = event.palletEvent.lastModified
                }
            } else {
                amount += pallet.amount
                if (pallet.lastModified > lastModified) {
                    lastModified = pallet.lastModified
                }
            }
        }
        return Product(
            name = event.palletEvent.product,
            items = amount,
            lastModified = lastModified
        )
    }
}