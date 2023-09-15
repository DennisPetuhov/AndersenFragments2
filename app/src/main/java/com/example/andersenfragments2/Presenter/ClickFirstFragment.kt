package com.example.twofragments

import com.example.andersenfragments.Data.Cat

interface ClickFirstFragment {
    fun onClick(cat: Cat)
    fun deleteCat(cat:Cat)
}