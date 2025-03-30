package com.practice.fixkan

import com.practice.fixkan.data.remote.repository.AuthRepository
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.fixkan.data.pref.UserPreference
import com.practice.fixkan.model.response.LoginResponse
import com.practice.fixkan.model.response.RegisterResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository, private val userPref: UserPreference) :
    ViewModel() {
    //    private val _loginState = mutableStateOf<Result<LoginResponse>?>(null)
//    val loginState: State<Result<LoginResponse>?> = _loginState
    private val _loginState = MutableStateFlow<Result<Unit>?>(null)
    val loginState: StateFlow<Result<Unit>?> = _loginState.asStateFlow()

    //    private val _registerState = mutableStateOf<Result<RegisterResponse>?>(null)
//    val registerState: State<Result<RegisterResponse>?> = _registerState
    private val _registerState = MutableStateFlow<Result<Unit>?>(null)
    val registerState: StateFlow<Result<Unit>?> = _registerState.asStateFlow()

    //    fun login(email: String, password: String) {
//        viewModelScope.launch {
//
//            try {
//                repository.login(email, password)
//                _loginState.value = Result.success(Unit)
//            } catch (e: Exception) {
//                _loginState.value = Result.failure(e)
//            }
//        }
//    }
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.login(email, password)

            // Simpan hasil loginState sesuai dengan result dari repository
            _loginState.value = result.mapCatching {
                if (it.status == "error") throw Exception(it.message)
            }
        }
    }


    fun register(
        name: String,
        email: String,
        password: String,
        province: String,
        district: String,
        subdistrict: String,
        village: String
    ) {
        viewModelScope.launch {
//            _registerState.value = repository.register(name, email, password, province, district, subdistrict, village)
            try {
//                repository.register(
//                    name,
//                    email,
//                    password,
//                    province,
//                    district,
//                    subdistrict,
//                    village
//                )
//                _registerState.value = Result.success(RegisterResponse(message = "Registration successful"))
                repository.register(
                    name,
                    email,
                    password,
                    province,
                    district,
                    subdistrict,
                    village
                )
                _registerState.value = Result.success(Unit)
            } catch (e: Exception) {
                _registerState.value = Result.failure(e)
            }
        }
    }

    fun resetRegisterState() {
        _registerState.value = null
    }

    fun resetLoginState() {
        _loginState.value = null
    }


    fun logout(onSucces: () -> Unit) {
        viewModelScope.launch {
            userPref.logout()
            onSucces()
        }
    }
}