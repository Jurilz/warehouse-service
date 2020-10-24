package com.warehouseservice.commands

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.warehouseservice.commands.orderCommands.CancelOrderCommand
import com.warehouseservice.commands.orderCommands.OrderCommand
import com.warehouseservice.commands.orderCommands.ValidateOrderCommand
import com.warehouseservice.commands.palletCommands.PalletCommand

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = PalletCommand::class, name = "PalletCommand"),
    JsonSubTypes.Type(value = OrderCommand::class, name = "OrderCommand"),
    JsonSubTypes.Type(value = ValidateOrderCommand::class, name = "ValidateOrderCommand"),
    JsonSubTypes.Type(value = CancelOrderCommand::class, name = "CancelOrderCommand")
)
abstract class Command {
    abstract fun execute()
}