package br.com.pedrofsn.kotlincoroutines

import retrofit2.http.GET

interface API {

    @GET("books")
    suspend fun getBooks(): List<Book>

}