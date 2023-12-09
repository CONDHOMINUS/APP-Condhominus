package com.example.condhominus.ui.register.condominium

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condhominus.model.AddressResponse
import com.example.condhominus.model.CondominiumRegister
import com.example.condhominus.model.CondominiumResponse
import com.example.condhominus.repository.AddressRepository
import com.example.condhominus.repository.RegisterRepository
import kotlinx.coroutines.launch

class RegisterCondominiumViewModel : ViewModel() {

    private val registerRepository = RegisterRepository()
    private val addressRepository = AddressRepository()

    val errorAddressLive = MutableLiveData<String>()
    val errorRegisterLive = MutableLiveData<String>()
    val condominiumLive = MutableLiveData<CondominiumResponse>()
    val addressLive = MutableLiveData<AddressResponse>()

    fun registerCondominium(condominiumRegister: CondominiumRegister) {
        viewModelScope.launch {
            try {
                registerRepository.registerCondominium(condominiumRegister)?.let {
                    condominiumLive.value = it
                } ?: run {
                    errorRegisterLive.value = "response null"
                }

            } catch (e: Exception) {
                errorRegisterLive.value = e.localizedMessage
            }
        }
    }

    fun getAddress(zipCode: String) {
        viewModelScope.launch {
            try {
                addressRepository.getAddress(zipCode)?.let {
                    addressLive.value = it
                } ?: run {
                    errorAddressLive.value = "response null"
                }

            } catch (e: Exception) {
                errorAddressLive.value = e.localizedMessage
            }
        }
    }
}