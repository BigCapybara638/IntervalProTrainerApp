package com.example.intervalprotrainerapp.di

import com.example.intervalprotrainerapp.data.DatabaseRepository
import com.example.intervalprotrainerapp.domain.repositories.TrainingRepository
import com.example.intervalprotrainerapp.domain.usecases.GetAllTrainingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    @ViewModelScoped
    fun provideTrainingRepository(): TrainingRepository {
        return DatabaseRepository()
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllTrainingUseCase(
        repository: TrainingRepository
    ): GetAllTrainingUseCase {
        return GetAllTrainingUseCase(repository)
    }
}