package com.knowledgespike

import kotlinx.coroutines.*

fun main() = runBlocking {

    processValue(3)

    print("Hello, ")
    Thread.sleep(1500)
}

suspend fun processValue(initialValue: Int) {
    val value = getAValue()
    val anotherValue = getAnotherValue(initialValue, value)
    useTheValue(anotherValue)
}

suspend fun getAValue(): Int {
    delay(2)
    return 3
}

suspend fun getAnotherValue(initialValue: Int, firstValue: Int): Int {
    delay(2)

    return firstValue * initialValue
}

suspend fun useTheValue(value: Int) {
    delay(2000)
    println(value)
}
