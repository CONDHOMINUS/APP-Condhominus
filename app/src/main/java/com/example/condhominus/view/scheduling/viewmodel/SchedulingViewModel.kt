package com.example.condhominus.view.scheduling.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condhominus.model.schedule.AvailableSchedulesResponse
import com.example.condhominus.model.schedule.ScheduleBody
import com.example.condhominus.model.schedule.ScheduleResponse
import com.example.condhominus.repository.AvailableSchedulesRepository
import kotlinx.coroutines.launch

class SchedulingViewModel : ViewModel() {

    private val availableSchedulesRepository = AvailableSchedulesRepository()
    val availableSchedulesLive = MutableLiveData<AvailableSchedulesResponse>()
    val toScheduleLive = MutableLiveData<ScheduleResponse>()
    val errorToScheduleLive = MutableLiveData<String>()
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

    fun toSchedule(scheduleBody: ScheduleBody) {
        viewModelScope.launch {
            try {
                availableSchedulesRepository.toSchedule(scheduleBody)?.let {
                    toScheduleLive.value = it
                } ?: run {
                    errorToScheduleLive.value = "response null"
                }
            } catch (e: Exception) {
                errorToScheduleLive.value = e.localizedMessage
            }
        }
    }
}