package com.knowledgespike.client

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.Future

val PORT = 9999

fun main() {
    val future: Future<Void>

    val client: AsynchronousSocketChannel = AsynchronousSocketChannel.open()
    val hostAddress = InetSocketAddress("localhost", PORT)
    future = client.connect(hostAddress)

    future.get()

    val br = BufferedReader(InputStreamReader(System.`in`))
    var line: String
    println("Message to server:")
    while (br.readLine().also { line = it } != null) {
        if (line == "bye") {
            println("End")
            break
        }

        val response = sendMessage(client, line)
        println("response from server: $response")
        println("Message to server:")
    }

}

fun sendMessage(client: AsynchronousSocketChannel, message: String): String {
    val byteMsg = message.toByteArray()
    val buffer = ByteBuffer.wrap(byteMsg)
    val writeResult: Future<Int> = client.write(buffer)
    writeResult.get()
    buffer.flip()
    val readResult = client.read(buffer)
    readResult.get()
    val echo = String(buffer.array()).trim { it <= ' ' }
    buffer.clear()
    return echo
}

