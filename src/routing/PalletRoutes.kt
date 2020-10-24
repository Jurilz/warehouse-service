package com.warehouseservice.routing

import com.warehouseservice.commands.CommandHandler
import com.warehouseservice.commands.palletCommands.CreatePalletCommand
import com.warehouseservice.commands.palletCommands.DeletePalletCommand
import com.warehouseservice.commands.palletCommands.PalletCommand
import com.warehouseservice.commands.palletCommands.UpdatePalletCommand
import com.warehouseservice.domain.Pallet
import com.warehouseservice.domain.buildPalletCommand
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import com.warehouseservice.services.PalletService

fun Routing.palletRoutes() {

    val commandHandler: CommandHandler by inject()
    val palletService: PalletService by inject()


    route("/pallets") {
        get {
            call.respond(palletService.getPallets())
        }
        get("/all") {
            call.respond(palletService.getAll())
        }
        post {
            val command: CreatePalletCommand = call.receive<CreatePalletCommand>()
            commandHandler.handleCommand(command)
            call.respond(command)
        }
        put {
            val command: UpdatePalletCommand = call.receive<UpdatePalletCommand>()
            commandHandler.handleCommand(command)
            call.respond(command)
        }
    }

    route("/location/{productName}"){
        get {
            val productName: String = call.parameters["productName"]!!
            val pallets: List<Pallet>? = palletService.getByProductName(productName)
            if (pallets != null) {
                call.respond(pallets)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
    route("/pallets/{storageLocation}"){
        get {
            val storageLocation: String = call.parameters["storageLocation"]!!
            val pallet: Pallet? = palletService.getByStorageLocation(storageLocation)
            if (pallet != null) {
                call.respond(pallet)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
    route("/pallets/{id}"){
        delete {
            val id: String = call.parameters["id"]!!
            val deleted: Pallet? = palletService.getById(id)
            if (deleted != null) {
                val deleteCommand: PalletCommand = deleted.buildPalletCommand()
                val deletePalletCommand = DeletePalletCommand(deleteCommand)
                commandHandler.handleCommand(deletePalletCommand)
                call.respond(deleted)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}