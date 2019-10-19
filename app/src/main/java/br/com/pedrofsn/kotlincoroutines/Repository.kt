package br.com.pedrofsn.kotlincoroutines

import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.UnknownHostException

class Repository(val onFailure: suspend (String) -> Unit) {

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

    suspend fun getBooks() = try {
        service.getBooks()
    } catch (e: Exception) {
        handleError(e)
        emptyList<Book>()
    }

    private suspend fun handleError(e: Exception) {
        when (e) {
            is HttpException -> {
                val error = "${e.code()} - ${e.message()}"
                onFailure.invoke(error)
            }
            is UnknownHostException -> {
                val error = "Verifique suas condições de internet\nToque para tentar novamente!"
                onFailure.invoke(error)
            }
            else -> onFailure.invoke("veja bem -> ${e.message}")
        }
    }

}