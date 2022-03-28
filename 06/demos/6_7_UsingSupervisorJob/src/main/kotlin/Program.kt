package com.knowledgespike

import kotlinx.coroutines.*


// 3. use supervisorScope to stop propagation to siblings
// notice that the exception is still reported - this is done by the
// default UncaughtExceptionHandler, which, for the JVM, reports the exception
// on the console

fun main(args: Array<String>) = runBlocking {

    val job = launch {
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

// 2. use coroutineScope to stop propogation to siblings
// val scope = CoroutineScope(SupervisorJob())

//fun main(args: Array<String>) = runBlocking {
//
//    val job = launch {
//        scope.launch {
//            doWork(1)
//        }
//
//        scope.launch {
//            doWork(2)
//        }
//
//
//        scope.launch {
//            delay(1000)
//            println("Throw")
//            throw Exception()
//        }
//
//        delay(5000)
//    }
//
//    job.join()
//
//}

// 1. SupervisorJob in launch does nothing
//fun main(args: Array<String>) = runBlocking {
//
//    val job = launch(SupervisorJob()) {
//
//        launch {
//            doWork(1)
//        }
//
//        launch {
//            doWork(2)
//        }
//
//
//        launch {
//            delay(2000)
//            println("Throw")
//            throw Exception()
//        }
//    }
//
//    job.join()
//
//}

suspend fun doWork(i: Int) {
    while (true) {
        print(i)
        delay(200)
    }
}


