import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main(args: Array<String>) = runBlocking {

    launch {
        delay(1000)
        println("World")
    }

    print("Hello, ")

    delay(2000)

    doWork()
}

// 1. Add this function
// 2. Add the test
suspend fun doWork() {
    // do something
    delay(1000)
    // do something else
}

