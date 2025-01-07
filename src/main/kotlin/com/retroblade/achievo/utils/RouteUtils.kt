package com.retroblade.achievo.utils

import com.retroblade.achievo.common.MethodResult
import com.retroblade.achievo.models.view.response.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory


const val AUTH_CONFIG_NAMESPACE = "auth-jwt"
private const val API_PATH = "/api"

val logger: Logger = LoggerFactory.getLogger("RouteUtils")

fun Application.apiRouting(routing: Route.() -> Unit) {
    routing {
        route(API_PATH, routing)
    }
}

fun Application.apiRoutingAuthorized(routing: Route.() -> Unit) {
    routing {
        route(API_PATH) {
            authenticate(AUTH_CONFIG_NAMESPACE, build = routing)
        }
    }
}

inline fun Route.getAutoResult(
    path: String,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(ApplicationCall) -> MethodResult<Any>
) {
    get(path) {
        callInternal(call){ body(call) }
    }
}

inline fun Route.postAutoResult(
    path: String,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(ApplicationCall) -> MethodResult<Any>
) {
    post(path) {
        callInternal(call) { body(call) }
    }
}

inline fun <reified Model : Any> Route.postAutoResult(
    path: String,
    validator: Validator<Model>,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(ApplicationCall, Model) -> MethodResult<Any>
) {
    post(path) {
        callInternal(call) {
            val model = call.receive(Model::class)
            validator.validate(model)
            body(call, model)
        }
    }
}

suspend inline fun callInternal(call: ApplicationCall, resultBlock: () -> MethodResult<Any>) {
    try {
        val result = resultBlock()
        when (result) {
            is MethodResult.Success -> {
                if (result.data.javaClass == Unit.javaClass) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        result.data
                    )
                }
            }

            is MethodResult.Error -> call.respond(
                result.httpCode,
                result.errorResponse
            )
        }
    } catch (e: RequestValidationException) {
        logger.error("An error occurred while validating JSON from ${call.request.path()}", e)
        call.respond(
            HttpStatusCode.BadRequest,
            ErrorResponse(
                code = HttpStatusCode.BadRequest.value,
                message = e.message
            )
        )
    } catch (e: BadRequestException) {
        logger.error("An error occurred while processing request ${call.request.path()}", e)
        call.respond(
            HttpStatusCode.BadRequest,
            ErrorResponse(
                code = HttpStatusCode.BadRequest.value,
                message = "Something went wrong with your request"
            )
        )
    } catch (e: Exception) {
        logger.error("An error occurred while processing request ${call.request.path()}", e)
        call.respond(
            HttpStatusCode.BadGateway,
            ErrorResponse(
                code = HttpStatusCode.BadGateway.value,
                message = "Something went wrong"
            )
        )
    }
}
