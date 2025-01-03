package com.retroblade.achievo.routing

import io.ktor.server.application.*

interface BaseController {
    fun setupRouting(application: Application)
}