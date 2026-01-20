package com.example.intervalprotrainerapp.domain.usecases

abstract class UseCase<in P, out T> {

    abstract suspend operator fun invoke(params: P) : T

}

abstract class NoParamsUseCase<out T> {

    abstract suspend operator fun invoke() : T

}
