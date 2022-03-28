package com.knowledgespike

import kotlinx.coroutines.*

/**
 * Talk about not using Global.launch and so creating a CoroutineScope
 * Show that launchParent is the parent of the job returned from scope.launch
 * Show that j1 == job and that j1 is the child of launchParent, this is always a 'Job'
 */


fun main() = runBlocking {
val launchParent = Job()
val scope = CoroutineScope(Job())
val job = scope.launch(launchParent) {
    val j1 = coroutineContext[Job]

    val j2 = launch {
        delay(500)
    }

        println("job passed to the scope.launch as the new context: $launchParent")
        displayChildren(0, launchParent)
        println("job returned from main launch (j1): $j1")
        displayChildren(0, j1!!)
        println("job returned from child launch (j2): $j2")
        displayChildren(0, j2)
        println()
        j2.join()
    }

//    println("launchJob: $job") -- same as j1
    job.join()

}

fun displayChildren(depth:Int = 0, job: Job) {
    job.children.forEach {
        for(i in 0..depth) {
            print("\t")
        }
        println("child: $it")
        displayChildren(depth+1, it)
    }
}