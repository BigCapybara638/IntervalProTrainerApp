package com.example.intervalprotrainerapp.data

import com.example.intervalprotrainerapp.models.TrainingItem
import kotlinx.coroutines.delay

class DatabaseRepository() {
    private val database = DataBase()

    suspend fun getList() : List<TrainingItem> {
        delay(2000)
        return database.list
    }
}