package com.example.condhominus.ui.register.tenant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condhominus.model.Tenant
import com.example.condhominus.model.TenantResponse
import com.example.condhominus.repository.RegisterRepository
import kotlinx.coroutines.launch

class RegisterTenantViewModel : ViewModel() {

    private val repository = RegisterRepository()

    val registerTenantLive: MutableLiveData<Pair<Boolean, TenantResponse?>> = MutableLiveData()
    val errorLive: MutableLiveData<String> = MutableLiveData()

    fun setTenant(tenant: Tenant) {
        viewModelScope.launch {
            try {
                registerTenantLive.value = Pair(true, null)
                val result = repository.registerTenant(tenant)

                result?.let {
                    registerTenantLive.value = Pair(false, it)
                } ?: run {
                    errorLive.value = "response null"
                }

            } catch (e: Exception) {
                errorLive.value = e.localizedMessage
            }
        }
    }
}