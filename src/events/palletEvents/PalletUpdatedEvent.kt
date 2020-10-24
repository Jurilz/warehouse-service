package com.warehouseservice.events.palletEvents

import com.warehouseservice.events.Event

class PalletUpdatedEvent(val palletEvent: PalletEvent): Event()