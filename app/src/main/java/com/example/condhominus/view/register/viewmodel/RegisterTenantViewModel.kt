package com.example.condhominus.view.register.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condhominus.model.condominium.CondominiumsListResponse
import com.example.condhominus.model.tenant.Tenant
import com.example.condhominus.model.tenant.TenantResponse
import com.example.condhominus.repository.RegisterRepository
import kotlinx.coroutines.launch

class RegisterTenantViewModel : ViewModel() {

    private val repository = RegisterRepository()

    val registerTenantLive: MutableLiveData<Pair<Boolean, TenantResponse?>> = MutableLiveData()
    val errorLive: MutableLiveData<String> = MutableLiveData()
    val errorCondominiumsLive: MutableLiveData<String> = MutableLiveData()
    val listCondominiumsLive: MutableLiveData<CondominiumsListResponse> = MutableLiveData()

    fun getCondominiums() {
        viewModelScope.launch {
            try {
                repository.getCondominiums()?.let {
                    listCondominiumsLive.value = it
                } ?: run {
                    errorCondominiumsLive.value = "response null"
                }

            } catch (e: Exception) {
                errorCondominiumsLive.value = e.localizedMessage
            }
        }
    }

    fun setTenant(tenant: Tenant) {
        viewModelScope.launch {
            try {
                registerTenantLive.value = Pair(true, null)
                repository.registerTenant(tenant)?.let {
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