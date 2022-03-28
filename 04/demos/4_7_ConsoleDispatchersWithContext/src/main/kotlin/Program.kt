package com.knowledgespike

import kotlinx.coroutines.*
import java.util.concurrent.Executors

val scope = CoroutineScope(Job())

val dispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
val executor = Executors.newFixedThreadPool(10)


fun main() = runBlocking<Unit>() {

    val jobs = arrayListOf<Job>()

    jobs += launch {
        println("          'default': In thread ${Thread.currentThread().name}")
    }
    jobs += launch(Dispatchers.Default) {
        doWork("'defaultDispatcher'")
    }
    jobs += launch(Dispatchers.IO) { // the 'IO' context'
        doWork("      IO Dispatcher")
    }
    jobs += launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
        doWork("       'Unconfined'")
    }
    jobs += launch(dispatcher) { // will get dispatched to ForkJoinPool.commonPool (or equivalent)
        doWork(" 'cachedThreadPool'")
    }
    jobs += launch(executor.asCoroutineDispatcher()) { // will get dispatched to ForkJoinPool.commonPool (or equivalent)
        doWork("  'fixedThreadPool'")
    }
    jobs += launch(newSingleThreadContext("OwnThread")) { // will get its own new thread
        doWork("           'newSTC'")
    }

    jobs.forEach { it.join() }


    println()
    println()
    println()

    executor.shutdownNow()
    dispatcher.close()

}

suspend fun doWork(dispatcherName: String) {
    withContext(Dispatchers.IO) {
        println("$dispatcherName: (But) in thread ${Thread.currentThread().name}")
    }
}

