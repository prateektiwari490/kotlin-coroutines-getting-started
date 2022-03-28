package com.knowlegespike.fxclient.views

import com.knowlegespike.fxclient.model.MessageItem
import com.knowlegespike.fxclient.model.MessageItemModel
import tornadofx.*

class MessageFragment : ListCellFragment<MessageItem>() {
    val message = MessageItemModel(itemProperty)

    override val root = hbox {
            label(message.receivedMessage) {
                useMaxSize = true
            }
        }

}
