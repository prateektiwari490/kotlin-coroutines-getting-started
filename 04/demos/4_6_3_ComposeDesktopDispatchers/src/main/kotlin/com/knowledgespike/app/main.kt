package com.knowledgespike.app

import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import java.util.concurrent.Executors

fun main() = Window(
    title = "Echo Client",
    size = IntSize(200, 400)
) {
    mainUI()
}

@Composable
fun mainUI() {
    val message: MutableState<String> = remember { mutableStateOf("") }
    val scope: CoroutineScope = rememberCoroutineScope()
    MaterialTheme {
        DesktopTheme {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .height(200.dp)
                    .padding(10.dp)
            ) {

                Text(message.value)
                Button(modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 10.dp),
                    onClick = {
                        scope.launch {
                            message.value = Thread.currentThread().name
                        }
                    }) { Text("No dispatcher") }

                Button(modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 10.dp),
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            message.value = Thread.currentThread().name
                        }
                    }) { Text("IO dispatcher") }

                Button(modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 10.dp),
                    onClick = {
                        scope.launch(Dispatchers.Default) {
                            message.value = Thread.currentThread().name
                        }
                    }) { Text("Default dispatcher") }
                Button(modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 10.dp),
                    onClick = {
                        scope.launch(Executors.newCachedThreadPool().asCoroutineDispatcher()) {
                            message.value = Thread.currentThread().name
                        }
                    }) { Text("Unconstrained dispatcher") }

                Button(modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 10.dp),
                    onClick = {
                        scope.launch(Dispatchers.Main) {
                            message.value = Thread.currentThread().name
                        }
                    }) { Text("Main dispatcher") }

            }
        }
    }
}

