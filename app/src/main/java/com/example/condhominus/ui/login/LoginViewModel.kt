package com.example.condhominus.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condhominus.model.LoginBody
import com.example.condhominus.model.LoginResponse
import com.example.condhominus.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val repository = LoginRepository()
    val loginLive = MutableLiveData<LoginResponse>()
    val errorLive: MutableLiveData<String> = MutableLiveData()

    fun userLogin(login: LoginBody) {
        viewModelScope.launch {
            try {
                repository.userLogin(login)?.let {
                    loginLive.value = it
                } ?: run {
                    errorLive.value = "Error message"
                }

            } catch (e: Exception) {
                errorLive.value = e.localizedMessage
            }
        }
    }
}