package com.example.practica_uno.nasa

import com.google.gson.annotations.SerializedName

data class NasaGson(
    @SerializedName("hdurl") var url: String,
    @SerializedName("title") var title: String
)

