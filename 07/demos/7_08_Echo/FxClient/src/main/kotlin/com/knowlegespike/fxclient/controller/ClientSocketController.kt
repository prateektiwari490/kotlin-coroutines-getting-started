package com.knowlegespike.fxclient.controller

import com.knowledgespike.wrappers.TcpSocket
import com.knowlegespike.fxclient.model.EchoItem
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import tornadofx.Controller
import tornadofx.SortedFilteredList
import tornadofx.getValue
import tornadofx.setValue
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel

class ClientSocketController : Controller() {
    val statusProperty = SimpleStringProperty()
    private var status: String by statusProperty

    val isConnectedProperty = SimpleBooleanProperty()
    private var isConnected: Boolean by isConnectedProperty

    val isRunningProperty = SimpleBooleanProperty()
    private var isRunning: Boolean by isRunningProperty


    val receivedMessages = SortedFilteredList<EchoItem>()

    private lateinit var tcpSocket: TcpSocket

    suspend fun connect(scope: CoroutineScope, port: String) {
        withContext(Dispatchers.JavaFx) {
            status = ""
            isConnected = false
        }

        val job = scope.launch(Dispatchers.IO) {
            val client = AsynchronousSocketChannel.open()
            val hostAddress = InetSocketAddress("localhost", port.toInt())
            tcpSocket = TcpSocket(client)
            tcpSocket.connect(hostAddress)
        }

        job.join()
        withContext(Dispatchers.JavaFx) {
            status = "Connected"
            isConnected = true
        }
    }

    suspend fun send(line: String) {

        supervisorScope {
            try {
                val result = async {
                    sendMessage(tcpSocket, line)
                }

                val response: String = result.await()

                if (line == "bye") {
                    println("End")
                    withContext(Dispatchers.IO) {
                        tcpSocket.close()
                    }
                }

                withContext(Dispatchers.JavaFx) {
                    receivedMessages.add(EchoItem(response))
                }
            } catch (t: Throwable) {
                println(t)
            }
        }
    }


    private suspend fun sendMessage(tcpSocket: TcpSocket, message: String): String {
        return withContext(Dispatchers.IO) {
            val byteMsg = message.toByteArray()
            val buffer = ByteBuffer.wrap(byteMsg)
            tcpSocket.write(buffer)
            buffer.flip()
            tcpSocket.read(buffer)

            val echo = String(buffer.array()).trim { it <= ' ' }
            buffer.clear()
            echo
        }
    }

    suspend fun repeat() {
        status = "Running"
        isRunning = true
        var counter = 0
        supervisorScope {
            launch(Dispatchers.IO) {
                try {
                    while (true) {
                        delay(750)
                        val result = async {
                            sendMessage(tcpSocket, "sending data ${counter++}")
                        }

                        val response: String = result.await()
                        withContext(Dispatchers.JavaFx) {
                            receivedMessages.add(EchoItem(response))
                        }
                    }
                } catch (t: CancellationException) {
                    withContext(NonCancellable) {
                        withContext(Dispatchers.JavaFx) {
                            receivedMessages.add(EchoItem("cancelled"))
                            status = "Cancelled"
                            isRunning = false
                        }
                    }
                }
            }
        }
    }
}