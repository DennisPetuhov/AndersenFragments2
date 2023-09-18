package com.example.andersenfragments2.Presenter

import com.example.andersenfragments2.Data.Cat

interface ClickFirstFragment {
    fun onClick(cat: Cat)
    fun deleteCat(cat: Cat)
}