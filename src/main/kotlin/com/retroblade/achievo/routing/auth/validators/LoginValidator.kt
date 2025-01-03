package com.retroblade.achievo.routing.auth.validators

import com.retroblade.achievo.models.view.request.LoginRequest
import com.retroblade.achievo.utils.RequestValidationException
import com.retroblade.achievo.utils.Validator

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