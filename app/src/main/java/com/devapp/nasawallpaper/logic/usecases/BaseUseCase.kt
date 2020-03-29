package com.devapp.nasawallpaper.logic.usecases

abstract class BaseUseCase<T> {
    abstract suspend fun run(): T;
}