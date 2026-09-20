package com.trilhacusto.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.local.AppDatabase
import com.trilhacusto.data.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.firstOrNull
import java.security.MessageDigest

class AuthViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val database: AppDatabase
) : ViewModel() {

    val isPluggyConfigured: StateFlow<Boolean?> = userPreferencesRepository.isPluggyConfigured
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
        
    val hasRegistered: StateFlow<Boolean?> = userPreferencesRepository.hasRegistered
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val userPasswordHash: StateFlow<String?> = userPreferencesRepository.userPasswordHash
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _passwordInput = MutableStateFlow("")
    val passwordInput: StateFlow<String> = _passwordInput.asStateFlow()

    fun updatePasswordInput(value: String) { _passwordInput.value = value }

    fun loginWithPassword(onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val rawPassword = _passwordInput.value.trim()
            val hashedInput = hashPassword(rawPassword)
            val savedHash = userPreferencesRepository.userPasswordHash.firstOrNull()
            if (hashedInput == savedHash) {
                onSuccess()
            } else {
                onError()
            }
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.joinToString("") { "%02x".format(it.toInt() and 0xFF) }
    }

    fun resetApp(onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            database.clearAllTables()
            userPreferencesRepository.clearAllData()
            kotlinx.coroutines.withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }
}
