package com.knowledgespike.server

import java.io.File
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future


class AsyncEchoServer {
    private var serverChannel: AsynchronousServerSocketChannel? = null
    private var acceptResult: Future<AsynchronousSocketChannel>? = null
    private var clientChannel: AsynchronousSocketChannel? = null
    fun runServer() {
        clientChannel = acceptResult!!.get()
        if (clientChannel != null && clientChannel!!.isOpen) {
            while (true) {
                var buffer = ByteBuffer.allocate(32)
                val readResult = clientChannel!!.read(buffer)

                // do some computation
                val result: Int = readResult.get()
                if(result == -1) break
                buffer.flip()
                val message = String(buffer.array()).trim { it <= ' ' }
                if (message == "bye") {
                    break // while loop
                }
                buffer = ByteBuffer.wrap(message.toByteArray())
                val writeResult = clientChannel!!.write(buffer)

                // do some computation
                writeResult.get()
                buffer.clear()
            } // while()
            clientChannel!!.close()
            serverChannel!!.close()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val server = AsyncEchoServer()
            server.runServer()
        }

    }

    init {
        try {
            serverChannel = AsynchronousServerSocketChannel.open()
            val hostAddress = InetSocketAddress("localhost", 9999)
            serverChannel!!.bind(hostAddress)
            acceptResult = serverChannel!!.accept()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}