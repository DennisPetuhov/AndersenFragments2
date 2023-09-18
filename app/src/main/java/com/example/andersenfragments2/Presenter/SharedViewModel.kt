package com.example.andersenfragments2.Presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.andersenfragments.Data.CatRepository
import com.example.andersenfragments2.Data.Cat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val _singleCat = MutableStateFlow(Cat(0, "", "", 0, ""))
    val singleCat: StateFlow<Cat> = _singleCat.asStateFlow()
    private val _listCat: MutableStateFlow<List<Cat>> = MutableStateFlow(mutableListOf())
    val listCat get() = _listCat
    val queryFlow = MutableStateFlow("")
    private val catRepository = CatRepository()
    private var mainList = catRepository.makeList()

    init {
        search()
    }


    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun search() {
        viewModelScope.launch {
            queryFlow.debounce(300)
                .filter { query ->
                    if (query.isEmpty()) {
                        _listCat.emit(mainList)
                        return@filter false
                    } else {
                        return@filter true
                    }
                }.distinctUntilChanged().flatMapLatest { query ->
                    searchViewQuery(query, mainList)
                }.flowOn(Dispatchers.Default)
                .collect { result ->
                    _listCat.emit(result)
                }
        }
    }

    private suspend fun searchViewQuery(text: String, list: List<Cat>): Flow<List<Cat>> {
        return flow {
            val filteredList = list.filter { it.surname.contains(text) || it.name.contains(text) }
            emit(filteredList)
        }
    }


    fun updateFlowOfSingleCat(cat: Cat) {
        _singleCat.value = cat
    }

    fun SendCatToFirstFragment(name: String, surname: String, phone: Int) {
        viewModelScope.launch {
            val id = _singleCat.value.id
            val url = _singleCat.value.url
            val newList = _listCat.value.map {
                if (it.id == id) {
                    it.copy(id, name = name, surname = surname, phone = phone)
                    val newCat = Cat(id, name, surname, phone, url)
                    newCat

                } else {
                    it
                }

            }
            mainList = newList
            _listCat.emit(newList)
        }
    }


    fun deleteCat(cat: Cat) {
        viewModelScope.launch {
            val filteredList = _listCat.value.filter {
                println(cat.id.toString())
                it.id != cat.id

            }
            _listCat.emit(filteredList)

        }

    }

}