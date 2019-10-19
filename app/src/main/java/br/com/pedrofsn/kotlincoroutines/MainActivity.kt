package br.com.pedrofsn.kotlincoroutines

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private val repository by lazy {
        Repository { error ->
            launch(Dispatchers.Main) {
                updateTextView(error)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.setOnClickListener {
            doRequest()
        }

    }

    private fun doRequest() {
        // Está implicitamente usando o coroutineContext da BaseActivity
        launch {
            logThreadName(tag = "launch") // main
            showProgressBar()

            // Processamento realizado em outra thread
            val books = async(IO) {
                logThreadName(tag = "request") // DefaultDispatcher-worker-1

                return@async repository.getBooks()
            }

            // Processamento realizado em outra thread
            val deferredNames = async(IO) {
                logThreadName(tag = "processing_name") // DefaultDispatcher-worker-2

                return@async books
                    .await()
                    .map { it.name }
                    .joinToString(", \n")
            }

            val names = deferredNames.await()
            if (names.isNotBlank()) {
                updateTextView(names)
            }
        }
    }

    private fun logThreadName(tag: String) = Thread.currentThread().name
        .let { "$tag: $it" }
        .run { println(this) }

    private fun updateTextView(message: String) {
        textView.text = message
        hideProgressBar()
    }

    private fun showProgressBar() {
        textView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        textView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }


}