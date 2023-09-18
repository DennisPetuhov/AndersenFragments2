package com.example.andersenfragments.Data

import com.example.andersenfragments2.Data.Cat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val URL = "https://source.unsplash.com/random/30x30="
const val PHONE = 2222
const val SURNAME = "the cat"
const val NAME = "keksik"


class CatRepository {

    fun makeList(): List<Cat> {
        var id = 0
        val newList = mutableListOf<Cat>()
        repeat(100) {
            id++
            val newCat = Cat(id, NAME, SURNAME, PHONE, URL)
            newList.add(newCat)
        }
        return newList
    }

    suspend fun getData(list: List<Cat>): Flow<List<Cat>> {
        return flow {
            delay(1000)
            emit(list)
        }
    }


}