package com.warehouseservice.utils

import java.time.ZoneId
import java.time.ZonedDateTime

class Util {

    companion object {
        fun getCurrentDate(): String {
            val nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));
            return nowUTC.toString()
        }
    }
}