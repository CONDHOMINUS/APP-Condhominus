package com.example.condhominus.ui.scheduling

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condhominus.model.AvailableSchedulesResponse
import com.example.condhominus.repository.AvailableSchedulesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SchedulingViewModel : ViewModel() {

    private val availableSchedulesRepository = AvailableSchedulesRepository()
    val availableSchedulesLive = MutableLiveData<AvailableSchedulesResponse>()
    val errorAvailableSchedulesLive = MutableLiveData<String>()

    fun getAvailableSchedules() {
        viewModelScope.launch {
            try {
                availableSchedulesRepository.getAvailableSchedules()?.let {
                    availableSchedulesLive.value = it
                } ?: run {
                    errorAvailableSchedulesLive.value = "response null"
                }

            } catch (e: Exception) {
                errorAvailableSchedulesLive.value = e.localizedMessage
            }
        }
    }
}