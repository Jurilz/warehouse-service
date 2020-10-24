package com.warehouseservice.domain

enum class PalletState {
    addedInFrontend,
    addedInBackend,
    storedInFrontend,
    storedInBackend,
    NEW_IN_SHOP_BACKEND,
    NEW_IN_WAREHOUSE_BACKEND,
    NEW_IN_WAREHOUSE_FRONTEND,
    PICKED_IN_WAREHOUSE_FRONTEND
}