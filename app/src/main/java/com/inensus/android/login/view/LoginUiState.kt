package com.inensus.android.login.view

sealed class LoginUiState {
    data class ServerUrl(
        val askForServerUrl: Boolean,
    ) : LoginUiState()

    object Success : LoginUiState()

    data class ValidationError(
        val errors: List<Error>,
    ) : LoginUiState() {
        sealed class Error {
            object EmailIsBlank : Error()

            object EmailIsNotInCorrectFormat : Error()

            object PasswordIsBlank : Error()
        }
    }
}
