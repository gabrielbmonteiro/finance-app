package com.trilhacusto.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _clientId = MutableStateFlow("")
    val clientId: StateFlow<String> = _clientId.asStateFlow()

    private val _clientSecret = MutableStateFlow("")
    val clientSecret: StateFlow<String> = _clientSecret.asStateFlow()

    private val _accountId = MutableStateFlow("")
    val accountId: StateFlow<String> = _accountId.asStateFlow()

    fun updateClientId(value: String) { _clientId.value = value }
    fun updateClientSecret(value: String) { _clientSecret.value = value }
    fun updateAccountId(value: String) { _accountId.value = value }

    fun saveCredentials(onComplete: () -> Unit) {
        viewModelScope.launch {
            userPreferencesRepository.savePluggyCredentials(
                clientId = _clientId.value,
                clientSecret = _clientSecret.value,
                accountId = _accountId.value
            )
            onComplete()
        }
    }
}
