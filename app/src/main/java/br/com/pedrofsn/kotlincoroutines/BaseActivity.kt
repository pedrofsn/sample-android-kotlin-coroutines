package br.com.pedrofsn.kotlincoroutines

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/*
    CREATED BY @PEDROFSN
*/

abstract class BaseActivity : AppCompatActivity(), CoroutineScope {

    val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

}