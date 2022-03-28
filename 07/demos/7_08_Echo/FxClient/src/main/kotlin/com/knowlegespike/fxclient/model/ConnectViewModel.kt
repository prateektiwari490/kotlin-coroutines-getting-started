package com.knowlegespike.fxclient.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class ConnectViewModel : ItemViewModel<Connect>() {
    val port = bind {item?.portProperty}
}

class Connect  {
    val portProperty: SimpleStringProperty = SimpleStringProperty()
    var port: String by portProperty
}
