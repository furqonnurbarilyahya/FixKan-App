package com.practice.fixkan.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.practice.fixkan.model.response.Location
import com.practice.fixkan.model.response.Response
import com.practice.fixkan.model.response.Role
import com.practice.fixkan.model.response.UpdatedAt
import com.practice.fixkan.model.response.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreference(private val dataStore: DataStore<Preferences>) {

    suspend fun saveUserData(user: User, accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = user.id
            preferences[USER_NAME_KEY] = user.name
            preferences[USER_EMAIL_KEY] = user.email
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[CREATED_AT_KEY] = user.location.createdAt
            preferences[PROVINCE_KEY] = user.location.province
            preferences[DISTRICT_KEY] = user.location.district
            preferences[SUBDISTRICT_KEY] = user.location.subdistrict
            preferences[VILLAGE_KEY] = user.location.village
            preferences[CREATED_ACCOUNT_KEY] = user.createdAt
        }
    }

    fun getUserData(): Flow<Response?> = dataStore.data.map { preferences ->
        val id = preferences[USER_ID_KEY] ?: ""
        val name = preferences[USER_NAME_KEY] ?: ""
        val email = preferences[USER_EMAIL_KEY] ?: ""
        val accessToken = preferences[ACCESS_TOKEN_KEY] ?: ""
        val refreshToken = preferences[REFRESH_TOKEN_KEY] ?: ""
        val createdAccount = preferences[CREATED_ACCOUNT_KEY] ?: ""


        val location = Location(
            createdAt = preferences[CREATED_AT_KEY] ?: "",
            id = preferences[LOCATION_ID_KEY] ?: "",
            province = preferences[PROVINCE_KEY] ?: "",
            district = preferences[DISTRICT_KEY] ?: "",
            subdistrict = preferences[SUBDISTRICT_KEY] ?: "",
            village = preferences[VILLAGE_KEY] ?: "",
            updatedAt = preferences[UPDATED_AT_KEY] ?: ""
        )

        if (name.isNotEmpty() && email.isNotEmpty()) {
            Response(
                accessToken = accessToken,
                refreshToken = refreshToken,
                user = User(
                    name = name,
                    email = email,
                    id = id,
                    password = "",
                    role = Role("", "", "", ""),
                    locationId = "",
                    roleId = "",
                    location = Location(
                        createdAt = location.createdAt,
                        id = location.id,
                        province = location.province,
                        district = location.district,
                        subdistrict = location.subdistrict,
                        village = location.village,
                        updatedAt = location.updatedAt
                    ),
                    refreshToken = refreshToken,
                    createdAt = createdAccount,
                    updatedAt = UpdatedAt("")
                )
            )
        } else {
            null
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val CREATED_ACCOUNT_KEY = stringPreferencesKey("created_account")

        private val LOCATION_ID_KEY = stringPreferencesKey("location_id")
        private val CREATED_AT_KEY = stringPreferencesKey("created_at")
        private val PROVINCE_KEY = stringPreferencesKey("province")
        private val DISTRICT_KEY = stringPreferencesKey("district")
        private val SUBDISTRICT_KEY = stringPreferencesKey("subdistrict")
        private val VILLAGE_KEY = stringPreferencesKey("village")
        private val UPDATED_AT_KEY = stringPreferencesKey("updated_at")


        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}

