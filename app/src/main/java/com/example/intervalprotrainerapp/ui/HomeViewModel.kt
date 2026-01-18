package com.example.intervalprotrainerapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intervalprotrainerapp.data.DatabaseRepository
import com.example.intervalprotrainerapp.models.TrainingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val databaseRepository = DatabaseRepository()

    private val _trainingList = MutableStateFlow<DataState<List<TrainingItem>>>(DataState.Loading)
    val trainingList: StateFlow<DataState<List<TrainingItem>>> = _trainingList

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                launch {
                    _trainingList.value = DataState.Loading
                    val result = databaseRepository.getList()
                    _trainingList.value = DataState.Success(result)
                }

            } catch (e: Exception) {
                println("Error loading")
                _trainingList.value = DataState.Error("$e")
            }

        }
    }
}

sealed class DataState<out t> {
    object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val message: String) : DataState<Nothing>()
}