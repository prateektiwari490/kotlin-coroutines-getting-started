package com.knowledgespike

import kotlinx.coroutines.*


fun main(args: Array<String>) = runBlocking {

    val result = async {
        delay(100)
        42
    }

    result.cancel()
    val answer = result.await()

}


