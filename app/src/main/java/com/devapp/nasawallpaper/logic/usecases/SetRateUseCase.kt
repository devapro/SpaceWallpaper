package com.devapp.nasawallpaper.logic.usecases

import com.devapp.nasawallpaper.storage.database.DataRepository

class SetRateUseCase(
    private val dataRepository: DataRepository
) :  BaseUseCase<Int>() {

    private var id: Int? = null
    private var rate: Int? = null

    fun setId(id: Int): SetRateUseCase {
        this.id = id
        return this
    }

    fun setRate(rate: Int): SetRateUseCase {
        this.rate = rate
        return this
    }

    override suspend fun run(): Int {
        if (id == null || rate == null){
            throw Exception("id and rate must be set")
        }
        return dataRepository.setRate(id!!, rate!!)
    }
}