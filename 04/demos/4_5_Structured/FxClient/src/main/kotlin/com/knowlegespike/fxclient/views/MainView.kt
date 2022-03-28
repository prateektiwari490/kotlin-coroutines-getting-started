package com.knowlegespike.fxclient.views

import com.knowlegespike.fxclient.Styles
import com.knowlegespike.fxclient.Styles.Companion.h1
import com.knowlegespike.fxclient.Styles.Companion.mainScreen
import com.knowlegespike.fxclient.controller.MessageController
import com.knowlegespike.fxclient.model.EchoMessageViewModel
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import tornadofx.*


class MainView : View("Main Window") {
    val model = EchoMessageViewModel()


    val messageController: MessageController by inject()

    override val root = vbox {
        addClass(mainScreen)
        padding = Insets(10.0, 10.0, 10.0, 10.0)

        alignment = Pos.CENTER
        label().addClass(Styles.logoIcon, Styles.icon, Styles.large)
        label(title).addClass(h1)

        form {
            maxWidth = 200.0
            fieldset(labelPosition = Orientation.VERTICAL) {
                fieldset("Data") {
                    listview(messageController.receivedMessages) {
                        cellFragment(MessageFragment::class)
                    }

                }
                button("Launch") {

                    isDefaultButton = true
                    useMaxWidth = true
                    action {
                        find(ChildView::class).openWindow()
                    }
                }
            }
        }
    }

}