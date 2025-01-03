package com.retroblade.achievo.routing.auth.validators

import com.retroblade.achievo.models.view.request.RefreshTokenRequest
import com.retroblade.achievo.utils.RequestValidationException
import com.retroblade.achievo.utils.Validator

class RefreshTokenValidator: Validator<RefreshTokenRequest> {

    override fun validate(model: RefreshTokenRequest) {
        if (model.refreshToken.isEmpty()) {
            throw RequestValidationException("Refresh token must not be empty")
        }

        if (model.accessToken.isEmpty()) {
            throw RequestValidationException("Access token must not be empty")
        }
    }
}