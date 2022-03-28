package com.knowledgespike.app

import androidx.compose.desktop.DesktopTheme
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import com.knowledgespike.network.*
import com.knowledgespike.wrappers.TcpSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


fun main() = application {

    val state: WindowState = rememberWindowState()
    state.size = WindowSize(600.dp, 800.dp)
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
    MaterialTheme {
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val snackbarCoroutineScope: CoroutineScope = rememberCoroutineScope()
        var message: String by remember { mutableStateOf("") }
        var isConnected by remember { mutableStateOf(false) }

        Scaffold(scaffoldState = scaffoldState) {
            DesktopTheme {
                val tcpSocket = remember { TcpSocket }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .height(200.dp)
                ) {
                    val scope: CoroutineScope = rememberCoroutineScope()
                    ConnectBox(isConnected) {
                        println("New port: $it")
                        scope.launch {

                            connect(port = it, tcpSocket = tcpSocket) {
                                when (it) {
                                    ConnectResult.Success -> isConnected = true
                                    else -> {
                                        isConnected = false
                                        tcpSocket.close()
                                    }
                                }

                                snackbarCoroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(it.message())
                                }
                            }
                        }
                    }
                    DataBox(tcpSocket, isConnected) { response ->
                        when (response) {
                            SocketResponse.ClosedResponse -> tcpSocket.close()
                            is SocketResponse.ErrorResponse -> {
                                tcpSocket.close()
                                isConnected = false
                                message = response.errorMessage
                            }
                            is SocketResponse.MessageResponse -> {/* do nothing*/
                            }
                        }
                    }
                    if (!message.isBlank()) {
                        snackbarCoroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ConnectBox(
    isConnected: Boolean,
    modifier: Modifier = Modifier
        .padding(vertical = 10.dp),
    onConnect: (String) -> Unit
) {
    var port by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }

    val text = if (isValid) "Port" else "Port*"
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .width(200.dp),
            value = port,
            onValueChange = {
                port = it
                isValid = it.toIntOrNull() != null
            },
            label = { Text(text) },
            isError = !isValid
        )
        Button(
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 10.dp),
            onClick = {
                if (isValid) {
                    onConnect(port)
                }
            }, enabled = (isValid && !port.isBlank() && !isConnected)
        ) {
            Text("Connect")
        }
    }
}

data class Message(val text: String)

@Composable
fun DataBox(
    tcpSocket: TcpSocket,
    isConnected: Boolean,
    modifier: Modifier = Modifier.fillMaxSize().padding(10.dp),
    callback: (SocketResponse) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        var messageToSend by remember { mutableStateOf("") }
        SingleDataBox(
            tcpSocket, isConnected, modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
                .border(1.dp, Color.Black)
                .padding(10.dp)
        ) {
            when (it) {
                is SocketResponse.ErrorResponse -> {
                    callback(it)
                }
                else -> {
                }
            }
        }

        Spacer(modifier = Modifier.padding(10.dp))
        ContinualDataBox(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
                .border(1.dp, Color.Black)
                .padding(10.dp), isConnected = isConnected, tcpSocket = tcpSocket
        ) {
            when (it) {
                is SocketResponse.ErrorResponse -> {
                    callback(it)
                }
                else -> {
                }
            }
        }
    }
}

@Composable
fun SingleDataBox(
    tcpSocket: TcpSocket,
    isConnected: Boolean,
    modifier: Modifier,
    callback: (SocketResponse) -> Unit
) {
    var messageToSend by remember { mutableStateOf("") }
    val messsages: MutableList<Message> = remember { mutableStateListOf() }
    val scope: CoroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = messageToSend,
                onValueChange = {
                    messageToSend = it
                },
                label = { Text("Message to send:") })
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                onClick = {
                    scope.launch {
                        send(tcpSocket, messageToSend) {
                            when (it) {
                                is SocketResponse.MessageResponse -> {
                                    messsages.add(Message(it.message))
                                }
                                else -> {
                                    callback(it)
                                }
                            }
                        }
                    }
                }, enabled = !messageToSend.isBlank() && isConnected
            ) {
                Text("Send")
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.Black),
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                itemsIndexed(messsages) { ndx, message ->
                    EchoItem(message = message, isOddRow = (ndx % 2) == 1)
                }
            }
        }
    }
}

@Composable
fun ContinualDataBox(
    tcpSocket: TcpSocket,
    isConnected: Boolean,
    modifier: Modifier = Modifier.fillMaxSize().padding(10.dp),
    callback: (SocketResponse) -> Unit
) {
    var isSending by remember { mutableStateOf(false) }
    val messsages: MutableList<Message> = remember { mutableStateListOf() }
    val scope: CoroutineScope = rememberCoroutineScope()
    var currentJob by remember { mutableStateOf<Job?>(null) }

    Box(
        modifier = modifier

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                onClick = {
                    currentJob = scope.launch {
                        repeatMessage(tcpSocket) {
                            when (it) {
                                is SocketResponse.MessageResponse -> {
                                    messsages.add(Message(it.message))
                                }
                                else -> {
                                    callback(it)
                                }
                            }
                        }
                    }
                    isSending = true
                }, enabled = isConnected && !isSending
            ) {
                Text("Go")
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                onClick = {
                    isSending = false
                    currentJob?.cancel()
                }, enabled = isConnected && isSending
            ) {
                Text("Cancel")
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.Black),
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                itemsIndexed(messsages) { ndx, message ->
                    EchoItem(message = message, isOddRow = (ndx % 2) == 1)
                }
            }
        }
    }
}


@Composable
fun EchoItem(
    message: Message,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
        .height(16.dp),
    style: TextStyle = TextStyle(
        fontSize = 20.sp,
        color = Color.DarkGray
    ),
    isOddRow: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            backgroundColor = if (isOddRow) Color(236, 236, 236) else Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = message.text,
                modifier = modifier,
                style = style
            )
        }
    }
}
