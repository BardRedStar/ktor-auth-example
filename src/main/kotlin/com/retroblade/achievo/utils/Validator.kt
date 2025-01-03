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

inline fun <reified Model: Any> Route.postValidated(
    path: String,
    validator: Validator<Model>,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(Model) -> Unit
) {
    post(path) {
        try {
            val model = call.receive<Model>()
            validator.validate(model)

            body(model)
        } catch (e: RequestValidationException) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    code = 1,
                    message = e.message
                )
            )
        } catch(e: JsonConvertException) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    code = 1,
                    message = "User not fould for such credentials"
                )
            )
        }
    }
}