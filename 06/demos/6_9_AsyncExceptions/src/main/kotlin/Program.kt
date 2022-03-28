package com.knowledgespike

import kotlinx.coroutines.*

val exceptionHandler = CoroutineExceptionHandler { context, exception ->
    println("Caught $exception")
}

val scope = CoroutineScope(Job())

// 2. async as non-root coroutine
//fun main(args: Array<String>) = runBlocking {
//
//
//    val job = scope.launch {
//        val deferred = async {
//            delay(1000)
//            throw Exception()
//        }
//
////        try {
////            deferred.await()
////        } catch (t: Throwable) {
////            println("Caught in await $t")
////        }
//    }
//
//    job.join()
//}


// 1. async as root coroutine
fun main(args: Array<String>) = runBlocking {


    val deferred = scope.async {
        delay(1000)
        throw Exception()
    }

    try {
        deferred.await()
    } catch (t: Throwable) {
        println("Caught $t")
    }
}





