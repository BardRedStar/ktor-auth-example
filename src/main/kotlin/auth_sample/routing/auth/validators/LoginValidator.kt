package auth_sample.routing.auth.validators

import auth_sample.models.view.request.LoginRequest
import auth_sample.utils.RequestValidationException
import auth_sample.utils.Validator

class LoginValidator: Validator<LoginRequest> {
    override fun validate(model: LoginRequest) {
        if (model.email.isEmpty()) {
            throw RequestValidationException("Поле email не должно быть пустым")
        }
        if (model.password.isEmpty()) {
            throw RequestValidationException("Поле password не должно быть пустым")
        }
    }
}