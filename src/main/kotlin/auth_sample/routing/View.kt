package auth_sample.routing

import io.ktor.server.application.*

interface View {
    fun setupRouting(application: Application)
}