package com.knowledgespike

import kotlinx.coroutines.*


fun main(args: Array<String>) = runBlocking {

    coroutineScope {
        val job = launch {
            launch {
                doWork(1)
            }

            launch {
                doWork(2)
            }

            launch {
                try {
                    delay(2000)
                    throw Exception("Some exception")
                } catch (t: Throwable) {
                    println("Caught exception")
                }
            }

        }

    }
    println("Scope ended")

}

suspend fun doWork(i: Int) {
    while (true) {
        print(i)
        delay(200)
    }
}


