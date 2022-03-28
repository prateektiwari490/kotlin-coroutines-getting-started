package com.knowledgespike

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main(args: Array<String>) = runBlocking {

    val job = launch {
        delay(1000)
        println("World")
    }

    print("Hello, ")

    job.join()

}



