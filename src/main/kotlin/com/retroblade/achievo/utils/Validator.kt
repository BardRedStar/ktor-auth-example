package com.retroblade.achievo.utils

import com.retroblade.achievo.models.view.response.ErrorResponse
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

interface Validator<Model> {
    fun validate(model: Model)
}

