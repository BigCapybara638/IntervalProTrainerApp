package com.example.intervalprotrainerapp.data

import com.example.intervalprotrainerapp.domain.models.TrainingItem
import com.example.intervalprotrainerapp.domain.repositories.TrainingRepository
import kotlinx.coroutines.delay

class DatabaseRepository() : TrainingRepository {
    private val database = DataBase
    override suspend fun getAllTraining(): List<TrainingItem> {
        return database.list
    }

    override suspend fun getTrainingById(id: Long): TrainingItem {
        TODO("Not yet implemented")
    }

    override suspend fun insertTraining(training: TrainingItem) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTraining(training: TrainingItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTraining(training: TrainingItem) {
        TODO("Not yet implemented")
    }

}