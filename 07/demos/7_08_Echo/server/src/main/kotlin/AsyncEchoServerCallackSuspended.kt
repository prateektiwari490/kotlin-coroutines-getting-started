package com.knowledgespike.serverasync

import com.knowledgespike.wrappers.TcpSocket
import com.knowledgespike.wrappers.asyncAccept
import kotlinx.coroutines.*
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel


fun main() = runBlocking {

    val serverChannel: AsynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()
    println("Server channel is $serverChannel")
    val hostAddress = InetSocketAddress("localhost", 9999)
    val channel: AsynchronousServerSocketChannel = serverChannel.bind(hostAddress)

    while (true) {
        println("waiting on ${channel}...")
        val deferred = async {
            serverChannel.asyncAccept()
        }

        val socketChannel: AsynchronousSocketChannel = deferred.await()

        println("Socket channel is $socketChannel")
        launch {
            processMessage(socketChannel)
        }
    }
}


suspend fun processMessage(socketChannel: AsynchronousSocketChannel) {
    val tcpSocket = TcpSocket(socketChannel)
    withContext(Dispatchers.IO) {
        while (true) {
            val buffer = ByteBuffer.allocate(1024)
            val result = tcpSocket.read(buffer)
            if(result == -1) break // End-of-stream
            buffer.flip()
            val message = String(buffer.array()).trim { it <= ' ' }
            if (message == "bye") {
                println("Break")
                tcpSocket.close()
                break // while loop
            } else {
                println("Message: $message")
            }
            tcpSocket.write(buffer)
            buffer.clear()
        }
    }
}


