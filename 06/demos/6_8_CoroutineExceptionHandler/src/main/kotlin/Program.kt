package com.knowledgespike

import kotlinx.coroutines.*

val exceptionHandler = CoroutineExceptionHandler { context, exception ->
    println("Caught $exception")
}

val scope = CoroutineScope(Job() + exceptionHandler)

fun main(args: Array<String>) = runBlocking {


    val job = scope.launch {

        supervisorScope {
            launch {
                doWork(1)
            }

            launch {
                doWork(2)
            }


            launch {
                delay(1000)
                println("Throw")
                throw Exception()
            }
        }
    }
    job.join()

}


suspend fun doWork(i: Int) {
    while (true) {
        print(i)
        delay(200)
    }
}


