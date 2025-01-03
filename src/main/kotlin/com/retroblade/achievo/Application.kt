package com.retroblade.achievo

import com.retroblade.achievo.di.AppComponentHolder
import com.retroblade.achievo.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDependencyGraph()
    configureSerialization()
    configureSecurity(AppComponentHolder.get().getTokenUtils())
    configureRouting()
}

fun Application.configureRouting() {
    listOf(
        AppComponentHolder.get().getAuthController(),
        AppComponentHolder.get().getProfileController()
    ).forEach {
        it.setupRouting(this)
    }
}

fun Application.configureDependencyGraph() {
    AppComponentHolder.set(AppComponentHolder.get())
}