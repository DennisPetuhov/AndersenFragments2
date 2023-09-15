package com.example.andersenfragments.Data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val URL = "https://source.unsplash.com/random/30x30="
const val PHONE = 2222
const val SURNAME = "The Cat"
const val NAME = "KEKSIK"


class CatRepository {

    suspend fun getData(): Flow<MutableList<Cat>> {
        return flow {
            var id = 0
            val newList = mutableListOf<Cat>()
            repeat(100) {
                id++
                val newCat = Cat(id, NAME, SURNAME, PHONE, URL)
                newList.add(newCat)


            }
            delay(1000)
            emit(newList)
        }
    }
}