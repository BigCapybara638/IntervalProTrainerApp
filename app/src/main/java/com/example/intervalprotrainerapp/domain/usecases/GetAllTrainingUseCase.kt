package com.example.intervalprotrainerapp.domain.usecases

import com.example.intervalprotrainerapp.data.DatabaseRepository
import com.example.intervalprotrainerapp.domain.models.TrainingItem
import com.example.intervalprotrainerapp.domain.repositories.TrainingRepository

/** Получить все тренировки */
class GetAllTrainingUseCase (
    private val repository: DatabaseRepository
) : NoParamsUseCase<List<TrainingItem>>() {
    override suspend fun invoke(): List<TrainingItem> {
        return repository.getAllTraining()
    }
}