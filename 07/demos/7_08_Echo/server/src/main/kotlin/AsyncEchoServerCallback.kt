package com.knowledgespike.server

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler


internal class ReadWriteHandler(val clientChannel: AsynchronousSocketChannel) :
    CompletionHandler<Int?, MutableMap<String, Any>> {
    override fun completed(result: Int?, attachment: MutableMap<String, Any>) {
        val action = attachment["action"] as String?
        if ("read" == action) {
            val buffer = attachment["buffer"] as ByteBuffer?
            buffer!!.flip()
            attachment["action"] = "write"
            clientChannel.write(buffer, attachment, this)
            buffer.clear()
        } else if ("write" == action) {
            val buffer = ByteBuffer.allocate(32)
            attachment["action"] = "read"
            attachment["buffer"] = buffer
            clientChannel.read(buffer, attachment, this)
        }
    }

    override fun failed(exc: Throwable, attachment: MutableMap<String, Any>) {}
}

fun main() {

    val serverChannel: AsynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()
    val hostAddress = InetSocketAddress("localhost", 9999)
    serverChannel.bind(hostAddress)
    while (true) {
        serverChannel.accept<Any?>(null, object : CompletionHandler<AsynchronousSocketChannel?, Any?> {
            override fun completed(asynchronousSocketChannel: AsynchronousSocketChannel?, attachment: Any?) {
                if (serverChannel.isOpen) serverChannel.accept<Any?>(null, this)

                if (asynchronousSocketChannel!!.isOpen) {
                    val handler = ReadWriteHandler(asynchronousSocketChannel)
                    val buffer = ByteBuffer.allocate(32)
                    val attachment: MutableMap<String, Any> = HashMap()
                    attachment["action"] = "read"
                    attachment["buffer"] = buffer
                    asynchronousSocketChannel.read(buffer, attachment, handler)
                }
            }

            override fun failed(exc: Throwable, attachment: Any?) {
                // process error
            }
        })
        System.`in`.read()
    }
}

