# Jobs and Parents

If you run the app you will see output something like this:

``` bash
scopeJob: JobImpl{Active}@1ae7b02a, 
### withoutLaunchContext #######
job passed to coroutineScope when that scope is created: SupervisorJobImpl{Active}@199db944
	child: "coroutine#2":StandaloneCoroutine{Active}@42eea6db
job returned from main launch: "coroutine#2":StandaloneCoroutine{Active}@42eea6db
job returned from child launch: "coroutine#3":StandaloneCoroutine{Completed}@61f3490e


############################################

### withLaunchContext #######
job passed to coroutineScope when that scope is created: SupervisorJobImpl{Active}@470224b8
job passed to the scope.launch as the new context: SupervisorJobImpl{Active}@69cc5cd2
	child: "coroutine#4":StandaloneCoroutine{Active}@48d447f2
job returned from main launch: "coroutine#4":StandaloneCoroutine{Active}@48d447f2
job returned from child launch: "coroutine#5":StandaloneCoroutine{Completed}@47fc0b14

```

The first part of the code creates a scope with a new job as its context but when it calls launch it does not pass a 
job to launch

``` kotlin
    val scopeJob = SupervisorJob()
    val scope = CoroutineScope(scopeJob)
    val job = scope.launch {

```

When you look at the output you see this 

``` bash 
scopeJob: JobImpl{Active}@1ae7b02a, 
### withoutLaunchContext #######
job passed to coroutineScope when that scope is created: SupervisorJobImpl{Active}@199db944
	child: "coroutine#2":StandaloneCoroutine{Active}@42eea6db
job returned from main launch: "coroutine#2":StandaloneCoroutine{Active}@42eea6db
job returned from child launch: "coroutine#3":StandaloneCoroutine{Completed}@61f3490e
```

So the parent of the job created by `launch` is the `SupervisorJob` passed to the `CoroutineScope` that is used to 
call `launch`

In the second part of the code you have this

```kotlin
    val scopeJob = SupervisorJob()
    val supervisorJob = SupervisorJob()
    val scope = CoroutineScope(scopeJob)
    val job = scope.launch(supervisorJob) {
```

and the output is

``` bash
### withLaunchContext #######
job passed to coroutineScope when that scope is created: SupervisorJobImpl{Active}@470224b8
job passed to the scope.launch as the new context: SupervisorJobImpl{Active}@69cc5cd2
	child: "coroutine#4":StandaloneCoroutine{Active}@48d447f2
job returned from main launch: "coroutine#4":StandaloneCoroutine{Active}@48d447f2
job returned from child launch: "coroutine#5":StandaloneCoroutine{Completed}@47fc0b14
```

In this case the parent of the job created by `launch` is the job passed to `scope.launch`