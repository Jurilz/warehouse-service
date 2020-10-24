package com.warehouseservice.events.palletEvents

import com.warehouseservice.events.Event

class PalletDeletedEvent(val palletEvent: PalletEvent): Event()