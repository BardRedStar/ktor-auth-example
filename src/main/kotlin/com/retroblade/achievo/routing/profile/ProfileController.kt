package com.retroblade.achievo.routing.profile

import com.retroblade.achievo.common.MethodResult
import com.retroblade.achievo.routing.BaseController
import com.retroblade.achievo.service.ProfileService
import com.retroblade.achievo.utils.getAutoResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import javax.inject.Inject

class ProfileController @Inject constructor(
    private val profileService: ProfileService
) : BaseController {

    override fun setupRouting(application: Application) {
        application.routing {
            authenticate("auth-jwt") {
                getAutoResult("/profile") {
                    val userId = call.principal<JWTPrincipal>()?.subject

                    if (userId != null) {
                        profileService.getProfile(userId)
                    } else {
                        MethodResult.error(HttpStatusCode.Forbidden, message = "User not allowed")
                    }
                }
            }
        }
    }
}