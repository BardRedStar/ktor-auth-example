package com.retroblade.achievo.routing

import io.ktor.server.application.*

interface View {
    fun setupRouting(application: Application)
}