package com.example.intervalprotrainerapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intervalprotrainerapp.domain.models.TrainingItem
import com.example.intervalprotrainerapp.domain.usecases.GetAllTrainingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllTrainingUseCases: GetAllTrainingUseCase
) : ViewModel() {

    /** Состяние для данных о тренировках */
    private val _trainingList = MutableStateFlow<DataState<List<TrainingItem>>>(DataState.Loading)
    val trainingList: StateFlow<DataState<List<TrainingItem>>> = _trainingList

    /** Общее состояние загрузки */
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    /** Общее состояние ошибок */
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    init {
        loadData()
    }

    /** Общая загрузка данных с помощью метода из [databaseRepository] */
    fun loadData() {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null

            try {
                _trainingList.value = DataState.Loading
                val result = getAllTrainingUseCases()
                _trainingList.value = DataState.Success(result)

            } catch (e: Exception) {
                println("Error loading")
                _trainingList.value = DataState.Error("$e")
                _errorState.value = "$e"
            } finally {
                _loadingState.value = false
            }

        }
    }
}

sealed class DataState<out t> {
    object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val message: String) : DataState<Nothing>()
}