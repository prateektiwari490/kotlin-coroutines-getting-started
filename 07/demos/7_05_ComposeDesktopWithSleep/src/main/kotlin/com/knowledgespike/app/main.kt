package com.knowledgespike.app

import androidx.compose.desktop.DesktopTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*

fun main() = application {

    val state: WindowState = rememberWindowState()
    state.size = WindowSize(200.dp, 300.dp)
    state.position = WindowPosition(Alignment.Center)

    Window(onCloseRequest = ::exitApplication,
        title = "Echo Client",
        state = state
    ) {

        mainUI()
    }
}

@Composable
fun mainUI() {
    val counter: MutableState<Int> = remember { mutableStateOf(0) }
    MaterialTheme {
        DesktopTheme {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .height(200.dp)
                    .padding(10.dp)
            ) {

                Text(counter.value.toString())
                Button(modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 10.dp),
                    onClick = {
                        increment(counter)
                    }) { Text("Click to increment")}
            }
        }
    }
}

fun increment(counter: MutableState<Int>) {

    Thread.sleep(3000)
    counter.value += 1

}


