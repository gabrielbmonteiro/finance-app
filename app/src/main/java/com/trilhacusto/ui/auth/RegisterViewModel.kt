package com.trilhacusto.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userPrefs: UserPreferencesRepository
) : ViewModel() {



    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()


    fun updatePassword(value: String) { _password.value = value }
    fun updateConfirmPassword(value: String) { _confirmPassword.value = value }

    fun register(onComplete: () -> Unit, onError: (String) -> Unit) {
        val p1 = _password.value.trim()
        val p2 = _confirmPassword.value.trim()
        
        if (p1 != p2) {
            onError("As senhas não coincidem.")
            return
        }
        if (p1.length < 4) {
            onError("A senha deve ter pelo menos 4 caracteres.")
            return
        }
        viewModelScope.launch {
            userPrefs.saveUserRegistration(p1)
            onComplete()
        }
    }
}
