package com.knowledgespike

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() = runBlocking {

    val job = launch {
        repeat(1000) {
            delay(100)
            print(".")
        }
    }

    delay(2500)
//    println()
    // 1. show job.cancel and job.join
//    job.cancel()
//    job.join()

    // 2. Show job.cancelAndJoin
    job.cancelAndJoin()
    println()
    println("done")
}



