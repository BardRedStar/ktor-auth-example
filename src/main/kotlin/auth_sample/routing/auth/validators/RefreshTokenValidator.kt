package auth_sample.routing.auth.validators

import auth_sample.models.view.request.RefreshTokenRequest
import auth_sample.utils.RequestValidationException
import auth_sample.utils.Validator

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