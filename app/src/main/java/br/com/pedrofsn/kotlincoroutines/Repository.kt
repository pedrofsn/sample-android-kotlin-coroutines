package br.com.pedrofsn.kotlincoroutines

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class Repository {

    private val service by lazy {
        val clientHttp = OkHttpClient().newBuilder().build()
        val converter = MoshiConverterFactory.create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://bibleapi.co/api/")
            .addConverterFactory(converter)
            .client(clientHttp)
            .build()

        retrofit.create(API::class.java)
    }

    suspend fun getBooks(): List<Book> {
        return service.getBooks()
    }

}