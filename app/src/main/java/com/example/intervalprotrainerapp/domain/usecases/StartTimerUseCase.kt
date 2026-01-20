package com.example.intervalprotrainerapp.domain.usecases

import com.example.intervalprotrainerapp.domain.models.TrainingItem

class StartTimerUseCase() : UseCase<TrainingItem, Unit>() {
    override suspend fun invoke(training: TrainingItem) {
        TODO("Not yet implemented")
    }
}