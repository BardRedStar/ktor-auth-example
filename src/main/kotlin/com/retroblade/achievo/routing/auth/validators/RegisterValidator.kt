package com.retroblade.achievo.routing.auth.validators

import com.retroblade.achievo.models.view.request.RegisterRequest
import com.retroblade.achievo.utils.RequestValidationException
import com.retroblade.achievo.utils.Validator

class RegisterValidator: Validator<RegisterRequest> {

    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-.]+\\.[A-Za-z0-9]{2,}\$"
    val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$"
    val userNameRegex = "^[+\\w]{8,}\$"

    override fun validate(model: RegisterRequest) {

        if (model.firstName.isEmpty()) {
            throw RequestValidationException("Поле first_name не должно быть пустым")
        }

        if (model.lastName.isEmpty()) {
            throw RequestValidationException("Поле last_name не должно быть пустым")
        }

        if (model.userName.isEmpty()) {
            throw RequestValidationException("Поле user_name не должно быть пустым")
        }

        if (!model.userName.matches(userNameRegex.toRegex())) {
            throw RequestValidationException("Поле user_name должно содержать только большие и маленькие латинские буквы, цифры и символ _")
        }

        if (model.email.isEmpty()) {
            throw RequestValidationException("Поле email не должно быть пустым")
        }

        if (!model.email.matches(emailRegex.toRegex())) {
            throw RequestValidationException("Поле email содержит неверный формат адреса эл. почты")
        }

        if (!model.password.matches(passwordRegex.toRegex())) {
            throw RequestValidationException(
                "Пароль должен содержать как минимум 8 символов, одну цифру, одну букву, и один спец.символ (@\$!%*#?&)"
            )
        }
    }
}