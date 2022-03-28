package com.knowlegespike.fxclient.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class EchoMessageViewModel : ItemViewModel<EchoMessage>() {
    val message = bind {item?.messageProperty}
}


class EchoMessage {
    val messageProperty: SimpleStringProperty = SimpleStringProperty()
    var message: String by messageProperty

}
