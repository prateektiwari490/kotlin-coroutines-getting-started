package com.knowledgespike

import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking() {

    var child1: Job? = null
    var child2: Job? = null
    var child3: Job? = null
    var child4: Job? = null
    var child5: Job? = null
    var child6: Job? = null

    coroutineScope {

        val job = GlobalScope.launch {

            child1 = launch {
                repeat(1000) {
                    Thread.sleep(1000)
                    print("1")
                    yield()
                }
            }

            child2 = launch {
                repeat(1000) {
                    if (!isActive) return@launch
                    Thread.sleep(1000)
                    print("2")
                }
            }

            child3 = launch {
                repeat(1000) {
                    Thread.sleep(1000)
                    if (!isActive) throw CancellationException()
                    print("3")
                }
            }

            child4 = launch {
                repeat(1000) {
                    Thread.sleep(1000)
                    ensureActive()
                    print("4")
                }
            }

            child5 = launch {
                try {
                    repeat(1000) {
                        Thread.sleep(1000)
                        ensureActive()
                        print("5")
                    }
                } catch (ex: CancellationException) {
                    println("Cancelled: ${ex.message}")
                }
            }

            try {
                child6 = launch {
                    repeat(1000) {
                        Thread.sleep(1000)
                        ensureActive()
                        print("6")
                    }
                }
            } catch (ex: CancellationException) {
                println("Cancelled: ${ex.message}")
            }

            repeat(1000) {
                delay(1000)
                print("0")
            }

        }

        delay(4000)
        child6?.cancelAndJoin()
        println()
        println("Job is cancelled: ${job.isCancelled}")
        println("Job is active: ${job.isActive}")

        job.join()

    }

    println("coroutineScope finished")

}


