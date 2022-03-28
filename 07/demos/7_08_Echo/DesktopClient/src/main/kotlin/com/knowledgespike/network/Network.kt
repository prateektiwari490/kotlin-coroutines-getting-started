package com.knowledgespike.network

import com.knowledgespike.wrappers.TcpSocket
import kotlinx.coroutines.*
import java.net.InetSocketAddress
import java.nio.ByteBuffer

sealed class ConnectResult {
    abstract fun message(): String

    object Error : ConnectResult() {
        override fun message(): String = "Failed to connect"
    }

    object Success : ConnectResult() {
        override fun message(): String = "Connected"
    }
}

sealed class SocketResponse {
    data class MessageResponse(val message: String) : SocketResponse()
    data class ErrorResponse(val errorMessage: String) : SocketResponse()
    object ClosedResponse : SocketResponse()
}

suspend fun connect(port: String, tcpSocket: TcpSocket, onConnect: (ConnectResult) -> Unit) {

    supervisorScope {
        withContext(Dispatchers.IO) {
            val hostAddress = InetSocketAddress("localhost", port.toInt())
            try {
                tcpSocket.connect(hostAddress)
                onConnect(ConnectResult.Success)
            } catch (t: Throwable) {
                t.printStackTrace()
                onConnect(ConnectResult.Error)
            }
        }
    }
}

suspend fun send(tcpSocket: TcpSocket, line: String, processMessage: (SocketResponse) -> Unit) {
    supervisorScope {
        try {
            val result = async {
                sendMessage(tcpSocket, line)
            }

            val response: String = result.await()

            if (line == "bye") {
                println("End")
                processMessage(SocketResponse.ClosedResponse)
            }

            processMessage(SocketResponse.MessageResponse(response))
        } catch (t: Throwable) {
            println(t)
            processMessage(SocketResponse.ErrorResponse(t.localizedMessage ?: t.toString()))
        }
    }
}


suspend fun repeatMessage(tcpSocket: TcpSocket, processMessage: (SocketResponse) -> Unit) {
    supervisorScope {
        try {
            while (true) {
                delay(750)
                val result = async {
                    sendMessage(tcpSocket, "Repeat message")
                }

                val response: String = result.await()


                processMessage(SocketResponse.MessageResponse(response))
            }
        } catch (t: Throwable) {
            if(t is CancellationException)
                throw t
            println(t)
            processMessage(SocketResponse.ErrorResponse(t.localizedMessage ?: t.toString()))
        }
    }

}

suspend fun sendMessage(tcpSocket: TcpSocket, message: String): String {
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
