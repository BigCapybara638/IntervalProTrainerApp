package com.example.intervalprotrainerapp.domain.repositories

import com.example.intervalprotrainerapp.domain.models.TimerState
import com.example.intervalprotrainerapp.domain.models.TrainingItem

interface TimerRepository {

    suspend fun getTimerState() : TimerState?

    suspend fun startTimer(training: TrainingItem)

    suspend fun stopTimer()

    suspend fun skipTimer()

}