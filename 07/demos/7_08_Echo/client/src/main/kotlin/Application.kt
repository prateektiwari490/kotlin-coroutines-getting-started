package com.knowledgespike.client

import com.knowledgespike.wrappers.TcpSocket
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.Future

val PORT = 9999

fun main() = runBlocking {

    val client: AsynchronousSocketChannel = AsynchronousSocketChannel.open()
    val hostAddress = InetSocketAddress("localhost", PORT)
    val tcpSocket = TcpSocket(client)
    tcpSocket.connect(hostAddress)

    val br = BufferedReader(InputStreamReader(System.`in`))
    var line: String
    println("Main name is: \t\t\t\t\t${Thread.currentThread().name}")
    println("Message to server:")
    while (br.readLine().also { line = it } != null) {
        val result = async {
            println("while:async name is: \t\t\t${Thread.currentThread().name}")
            sendMessage(tcpSocket, line)
        }
        println("while: name is: \t\t\t\t${Thread.currentThread().name}")
        if (line == "bye") {
            println("End")
            break
        }
        val response: String = result.await()
        withContext(Dispatchers.Default) {
            println("withContext[Default] name is: \t${Thread.currentThread().name}")
            println("response from server: $response")
            println("Message to server:")
        }
    }
}

suspend fun sendMessage(tcpSocket: TcpSocket, message: String): String {
    return withContext(Dispatchers.IO) {
        println("withContext[IO]:1 name is: \t\t${Thread.currentThread().name}")

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

