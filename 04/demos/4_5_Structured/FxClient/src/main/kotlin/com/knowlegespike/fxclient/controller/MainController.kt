package com.knowlegespike.fxclient.controller

import com.knowlegespike.fxclient.model.MessageItem
import tornadofx.Controller
import tornadofx.SortedFilteredList

class MessageController : Controller() {
    val receivedMessages = SortedFilteredList<MessageItem>()
}