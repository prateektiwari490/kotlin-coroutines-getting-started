package com.knowlegespike.fxclient.views

import com.knowlegespike.fxclient.Styles
import com.knowlegespike.fxclient.Styles.Companion.h1
import com.knowlegespike.fxclient.Styles.Companion.mainScreen
import com.knowlegespike.fxclient.controller.ClientSocketController
import com.knowlegespike.fxclient.model.ConnectViewModel
import com.knowlegespike.fxclient.model.EchoMessageViewModel
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import kotlinx.coroutines.*
import tornadofx.*
import kotlin.coroutines.CoroutineContext


class MainView : View("Socket Client"), CoroutineScope {
    val model = EchoMessageViewModel()
    val connectModel = ConnectViewModel()


    val clientSocketController: ClientSocketController by inject()

    val coroutineScope: CoroutineScope
    init {
        coroutineScope = this
    }
    override val root = vbox {
        addClass(mainScreen)
        padding = Insets(10.0, 10.0, 10.0, 10.0)

        alignment = Pos.CENTER
        label().addClass(Styles.logoIcon, Styles.icon, Styles.large)
        label(title).addClass(h1)

        form {
            maxWidth = 200.0
            fieldset(labelPosition = Orientation.VERTICAL) {
                fieldset("Port") {
                    textfield(connectModel.port).validator {
                        if (it.isNullOrBlank()) {
                            error("The port field is required")
                        } else
                            if (it.toIntOrNull() == null) {
                                error("The value must be an integer")
                            } else null
                    }
                }
                button("Connect") {
                    enableWhen(connectModel.valid)
                    isDefaultButton = true
                    useMaxWidth = true
                    action {
                        launch {
                            clientSocketController.connect(this, connectModel.port.value)
                        }
                    }
                }
            }
        }
        hbox {
            vbox {
                form {
                    fieldset(labelPosition = Orientation.VERTICAL) {
                        fieldset("Message to Send") {
                            textarea(model.message).required()
                        }

                        button("Send") {
                            enableWhen(model.valid and clientSocketController.isConnectedProperty)
                            isDefaultButton = true
                            useMaxWidth = true
                            action {
                                launch {
                                    clientSocketController.send(model.message.value)
                                }
                            }
                        }
                    }
                }
                listview(clientSocketController.receivedMessages) {
                    cellFragment(EchoFragment::class)
                }
            }
            form {
                minWidth = 200.0
                fieldset(labelPosition = Orientation.VERTICAL) {
                    fieldset("Continual") {
                        button("Go") {
                            useMaxWidth = true
                            enableWhen(!clientSocketController.isRunningProperty)
                            action {
                                launch {
                                    clientSocketController.repeat()
                                }
                            }
                        }
                        button("Cancel") {
                            useMaxWidth = true
                            enableWhen(clientSocketController.isRunningProperty)
                            action {
                                job.cancel()
                                job = Job()
                            }
                        }
                    }
                }
            }
        }
        vbox {
            label(clientSocketController.statusProperty) {
                style {
                    paddingTop = 10
                    textFill = Color.RED
                    fontWeight = FontWeight.BOLD
                }
            }
        }
    }

    override fun onDelete() {
        job.cancel()
    }

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
}