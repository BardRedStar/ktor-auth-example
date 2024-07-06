package auth_sample

import auth_sample.di.AppComponentHolder
import auth_sample.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDependencyGraph()
    configureSerialization()
    configureSecurity(AppComponentHolder.get().getTokenService())
    configureRouting()
}

fun Application.configureRouting() {
    listOf(
        AppComponentHolder.get().getAuthView(),
        AppComponentHolder.get().getProfileView()
    ).forEach {
        it.setupRouting(this)
    }
}

fun Application.configureDependencyGraph() {
    AppComponentHolder.set(AppComponentHolder.get())
}