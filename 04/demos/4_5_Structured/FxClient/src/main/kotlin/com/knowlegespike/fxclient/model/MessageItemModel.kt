package com.knowlegespike.fxclient.model

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class MessageItem(message: String) {
    val messageProperty: SimpleStringProperty = SimpleStringProperty(message)
    var message: String by messageProperty

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as MessageItem

        return message == other.message
    }

    override fun hashCode(): Int {
        return message.hashCode()
    }
}

class MessageItemModel(property: ObjectProperty<MessageItem>) : ItemViewModel<MessageItem>(itemProperty = property) {
    val receivedMessage = bind(autocommit = true) { item?.messageProperty }
}

