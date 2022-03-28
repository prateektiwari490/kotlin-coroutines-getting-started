package com.knowledgespike

import kotlinx.coroutines.*


fun main(args: Array<String>) = runBlocking<Unit> {

    launch {
        runWithLocalScope()
        println("runWithLocalScope returned")
    }

}

suspend fun runWithLocalScope() {
    coroutineScope {
        launch {
            println("Launch 1")
            delay(1000)
        }
        launch {
            println("Launch 2")
            delay(2000)
        }
    }
    println("runWithLocalScope finished")

}

suspend fun runWithGlobalScope() {
    GlobalScope.launch {
        println("Launch 1")
        delay(1000)
    }
    GlobalScope.launch {
        println("Launch 2")
        delay(2000)
    }
    println("runWithGlobalScope finished")
}

