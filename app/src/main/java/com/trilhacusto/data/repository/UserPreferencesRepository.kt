package com.trilhacusto.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.MessageDigest

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        val PLUGGY_CLIENT_ID = stringPreferencesKey("pluggy_client_id")
        val PLUGGY_CLIENT_SECRET = stringPreferencesKey("pluggy_client_secret")
        val PLUGGY_ACCOUNT_ID = stringPreferencesKey("pluggy_account_id")
        val USER_PASSWORD_HASH = stringPreferencesKey("user_password_hash")
        val HAS_REGISTERED = booleanPreferencesKey("has_registered")
        val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
    }

    val pluggyClientId: Flow<String?> = context.dataStore.data.map { it[PLUGGY_CLIENT_ID] }
    val pluggyClientSecret: Flow<String?> = context.dataStore.data.map { it[PLUGGY_CLIENT_SECRET] }
    val pluggyAccountId: Flow<String?> = context.dataStore.data.map { it[PLUGGY_ACCOUNT_ID] }
    
    val hasRegistered: Flow<Boolean> = context.dataStore.data.map { it[HAS_REGISTERED] ?: false }
    val userPasswordHash: Flow<String?> = context.dataStore.data.map { it[USER_PASSWORD_HASH] }
    val lastSyncTime: Flow<Long?> = context.dataStore.data.map { it[LAST_SYNC_TIME] }

    val isPluggyConfigured: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            !preferences[PLUGGY_CLIENT_ID].isNullOrEmpty() &&
            !preferences[PLUGGY_CLIENT_SECRET].isNullOrEmpty() &&
            !preferences[PLUGGY_ACCOUNT_ID].isNullOrEmpty()
        }

    suspend fun savePluggyCredentials(clientId: String, clientSecret: String, accountId: String) {
        context.dataStore.edit { preferences ->
            preferences[PLUGGY_CLIENT_ID] = clientId
            preferences[PLUGGY_CLIENT_SECRET] = clientSecret
            preferences[PLUGGY_ACCOUNT_ID] = accountId
        }
    }

    suspend fun saveLastSyncTime(time: Long) {
        context.dataStore.edit { preferences ->
            preferences[LAST_SYNC_TIME] = time
        }
    }

    suspend fun saveUserRegistration(passwordRaw: String) {
        val hashed = hashPassword(passwordRaw)
        context.dataStore.edit { preferences ->
            preferences[USER_PASSWORD_HASH] = hashed
            preferences[HAS_REGISTERED] = true
        }
    }

    suspend fun updatePassword(newPasswordRaw: String) {
        val hashed = hashPassword(newPasswordRaw)
        context.dataStore.edit { preferences ->
            preferences[USER_PASSWORD_HASH] = hashed
        }
    }
    
    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.joinToString("") { "%02x".format(it.toInt() and 0xFF) }
    }
}
