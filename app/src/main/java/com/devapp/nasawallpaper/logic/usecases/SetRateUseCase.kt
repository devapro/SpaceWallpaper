package com.devapp.nasawallpaper.logic.usecases

import com.devapp.nasawallpaper.storage.database.DataRepository

class SetRateUseCase(
    private val dataRepository: DataRepository,
    private val id: Int,
    private val rate: Int
) :  BaseUseCase<Int>() {
    override suspend fun run(): Int {
        return dataRepository.setRate(id, rate)
    }
}