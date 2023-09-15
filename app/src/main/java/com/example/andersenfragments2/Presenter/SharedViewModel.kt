package com.example.twofragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.andersenfragments.Data.Cat
import com.example.andersenfragments.Data.CatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val _singleCat = MutableStateFlow<Cat>(Cat(0, "", "", 0, ""))
    val singleCat: StateFlow<Cat> = _singleCat.asStateFlow()

    private val _listCat: MutableStateFlow<List<Cat>> = MutableStateFlow(mutableListOf())
    val listCat get() = _listCat

    private val catRepository = CatRepository()

    fun getCats() {
        viewModelScope.launch {
            catRepository.getData().collect {
                _listCat.emit(it)
            }
        }
    }

    fun updateFlowOfSingleCat(cat: Cat) {
        _singleCat.value = cat
    }

    fun SendCatToFirstFragment(name: String, surname: String, phone: Int) {
        viewModelScope.launch {
            val id = _singleCat.value.id
            val newFlow = _listCat.value.map {
                if (it.id == id) {
                    it.copy(id, name = name, surname = surname, phone = phone)
                    val newCat = Cat(id, name, surname, phone, "")
                    newCat

                } else {
                    it
                }

            }
            _listCat.emit(newFlow)
        }
    }

    fun searchCat(p0: CharSequence?) {
        viewModelScope.launch {
           
            val list = _listCat.value
            if (!p0.isNullOrBlank()) {
                val filteredList = _listCat.value.filter {
                    it.name.contains(p0) || it.surname.contains(p0)
                }

                _listCat.emit(filteredList)
            } else {
//                _listCat.emit(list)
//                println("MYLIST"+list)
//

                getCats()
            }
        }

    }

    fun deleteCat(cat: Cat) {
        viewModelScope.launch {
           val filteredList = _listCat.value.filter {
               println(cat.id.toString())
               it.id !=cat.id

            }
            _listCat.emit(filteredList)

        }

    }



}