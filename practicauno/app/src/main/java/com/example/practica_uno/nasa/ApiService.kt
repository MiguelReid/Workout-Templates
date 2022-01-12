package com.example.practica_uno.nasa

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET
suspend fun getDatosNasa(@Url url: String): Response<NasaGson>
}