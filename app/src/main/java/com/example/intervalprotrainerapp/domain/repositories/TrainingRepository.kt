package com.example.intervalprotrainerapp.domain.repositories

import com.example.intervalprotrainerapp.domain.models.TrainingItem

interface TrainingRepository {

    suspend fun getAllTraining() : List<TrainingItem>

    suspend fun getTrainingById(id: Long) : TrainingItem

    suspend fun insertTraining(training: TrainingItem)

    suspend fun updateTraining(training: TrainingItem)

    suspend fun deleteTraining(training: TrainingItem)
}