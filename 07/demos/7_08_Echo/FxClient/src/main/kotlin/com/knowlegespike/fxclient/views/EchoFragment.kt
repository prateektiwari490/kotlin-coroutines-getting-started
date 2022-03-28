package com.knowlegespike.fxclient.views

import com.knowlegespike.fxclient.model.EchoItem
import com.knowlegespike.fxclient.model.EchoItemModel
import javafx.scene.Parent
import javafx.scene.layout.Priority
import tornadofx.*

class EchoFragment : ListCellFragment<EchoItem>() {
    val echoMessage = EchoItemModel(itemProperty)

    override val root = hbox {
            label(echoMessage.receivedMessage) {
                useMaxSize = true
            }
        }

}
