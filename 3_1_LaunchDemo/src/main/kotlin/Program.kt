package com.knowledgespike

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {

    // 1. Use launch

    GlobalScope.launch {
        delay(1000)
        println("World")
    }

    print("Hello, ")
    Thread.sleep(1500)

    // 2. Cannot call 'delay' here
    // delay(1000)


}