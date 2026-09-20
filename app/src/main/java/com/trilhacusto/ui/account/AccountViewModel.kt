package com.trilhacusto.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userPrefs: UserPreferencesRepository
) : ViewModel() {

    val clientId: StateFlow<String?> = userPrefs.pluggyClientId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
        
    val clientSecret: StateFlow<String?> = userPrefs.pluggyClientSecret
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
        
    val accountId: StateFlow<String?> = userPrefs.pluggyAccountId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword.asStateFlow()

    private val _confirmNewPassword = MutableStateFlow("")
    val confirmNewPassword: StateFlow<String> = _confirmNewPassword.asStateFlow()

    fun updateNewPassword(value: String) { _newPassword.value = value }
    fun updateConfirmNewPassword(value: String) { _confirmNewPassword.value = value }

    fun updatePluggyCredentials(newClientId: String, newClientSecret: String, newAccountId: String) {
        viewModelScope.launch {
            userPrefs.savePluggyCredentials(newClientId, newClientSecret, newAccountId)
        }
    }

    fun updatePassword(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (_newPassword.value != _confirmNewPassword.value) {
            onError("Senhas não coincidem.")
            return
        }
        if (_newPassword.value.length < 4) {
            onError("A senha deve ter pelo menos 4 caracteres.")
            return
        }
        viewModelScope.launch {
            userPrefs.updatePassword(_newPassword.value)
            _newPassword.value = ""
            _confirmNewPassword.value = ""
            onSuccess()
        }
    }
}
