package com.knowlegespike.fxclient.views

import com.knowlegespike.fxclient.controller.MessageController
import com.knowlegespike.fxclient.model.MessageItem
import kotlinx.coroutines.*
import tornadofx.View
import tornadofx.vbox

class ChildView : View("Child Window") {
    val messageController: MessageController by inject()
    lateinit var coroutineScope: CoroutineScope
    var count = 0

    override val root = vbox {
    }

    override fun onDock() {
        super.onDock()
        coroutineScope = MainScope()
        coroutineScope.launch {
            while(true) {
                delay(1000)
                count++
                messageController.receivedMessages.add(MessageItem("count $count"))
            }
        }
    }

    override fun onUndock() {
        super.onUndock()
        coroutineScope.cancel()
    }


}