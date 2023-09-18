package com.example.andersenfragments2.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cat(
    var id: Int,
    var name: String,
    var surname: String,
    var phone: Int,
    val url: String
) : Parcelable