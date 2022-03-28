package com.knowledgespike

import kotlinx.coroutines.*
import java.util.concurrent.Executors

val scope = CoroutineScope(Job())

val dispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
val executor = Executors.newFixedThreadPool(10)


fun main() = runBlocking<Unit>() {

    val jobs = arrayListOf<Job>()

    jobs += launch { // the 'default' context'
        println("          'default': In thread ${Thread.currentThread().name}")
    }
    jobs += launch(Dispatchers.Default) { // the 'default' context'
        println("'defaultDispatcher': In thread ${Thread.currentThread().name}")
    }
    jobs += launch(Dispatchers.IO) { // the 'IO' context'
        println("      IO Dispatcher: In thread ${Thread.currentThread().name}")
    }
    jobs += launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
        println("       'Unconfined': In thread ${Thread.currentThread().name}")
    }
    jobs += launch(coroutineContext) { // context of the parent, runBlocking coroutine
        println(" 'coroutineContext': In thread ${Thread.currentThread().name}")
    }
    jobs += launch(dispatcher) { // will get dispatched to ForkJoinPool.commonPool (or equivalent)
        println(" 'cachedThreadPool': In thread ${Thread.currentThread().name}")
    }
    jobs += launch(executor.asCoroutineDispatcher()) { // will get dispatched to ForkJoinPool.commonPool (or equivalent)
        println("  'fixedThreadPool': In thread ${Thread.currentThread().name}")
    }
    jobs += launch(newSingleThreadContext("OwnThread")) { // will get its own new thread
        println("           'newSTC': In thread ${Thread.currentThread().name}")
    }

    jobs.forEach { it -> it.join() }

    println()
    println()
    println()

    Thread.sleep(100)

    val job1 = scope.launch {
        val jobs = arrayListOf<Job>()

        jobs += launch { // the 'default' context'
            println("          'default': In thread ${Thread.currentThread().name}")
        }
        jobs += launch(Dispatchers.Default) { // the 'default' context'
            println("'defaultDispatcher': In thread ${Thread.currentThread().name}")
        }
        jobs += launch(Dispatchers.IO) { // the 'IO' context'
            println("      IO Dispatcher: In thread ${Thread.currentThread().name}")
        }
        jobs += launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
            println("       'Unconfined': In thread ${Thread.currentThread().name}")
        }
        jobs += launch(coroutineContext) { // context of the parent, runBlocking coroutine
            println(" 'coroutineContext': In thread ${Thread.currentThread().name}")
        }
        jobs += launch(dispatcher) { // will get dispatched to ForkJoinPool.commonPool (or equivalent)
            println(" 'cachedThreadPool': In thread ${Thread.currentThread().name}")
        }
        jobs += launch(executor.asCoroutineDispatcher()) { // will get dispatched to ForkJoinPool.commonPool (or equivalent)
            println("  'fixedThreadPool': In thread ${Thread.currentThread().name}")
        }
        jobs += launch(newSingleThreadContext("OwnThread")) { // will get its own new thread
            println("           'newSTC': In thread ${Thread.currentThread().name}")
        }

        jobs.forEach { it -> it.join() }

    }
    job1.join()

    // if you don't shut down the services then the main thread never ends
    executor.shutdownNow()
    dispatcher.close()

}

fun Job.status(): String = when {
    isCancelled -> "cancelled"
    isActive -> "Active"
    isCompleted -> "Complete"
    else -> "Nothing"
}

