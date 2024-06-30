package auth_sample.routing.auth.validators

import auth_sample.models.view.request.RegisterRequest
import auth_sample.utils.RequestValidationException
import auth_sample.utils.Validator

class RegisterValidator: Validator<RegisterRequest> {

    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-.]+\\.[A-Za-z0-9]{2,}\$"
    val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$"

    override fun validate(model: RegisterRequest) {

        if (model.firstName.isEmpty()) {
            throw RequestValidationException("Поле firstName не должно быть пустым")
        }

        if (model.lastName.isEmpty()) {
            throw RequestValidationException("Поле lastName не должно быть пустым")
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