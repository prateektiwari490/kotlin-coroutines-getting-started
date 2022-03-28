package com.knowlegespike.fxclient.views

import com.knowlegespike.fxclient.Styles
import com.knowlegespike.fxclient.Styles.Companion.h1
import com.knowlegespike.fxclient.Styles.Companion.mainScreen
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import tornadofx.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext


class MainView : View("Dispatchers"), CoroutineScope {

    override val root = vbox {
        addClass(mainScreen)
        padding = Insets(10.0, 10.0, 10.0, 10.0)

        alignment = Pos.CENTER
        label().addClass(Styles.logoIcon, Styles.icon, Styles.large)
        label(title).addClass(h1)

        form {
            maxWidth = 200.0
            fieldset(labelPosition = Orientation.VERTICAL) {
                button("Run - no dispatcher") {
                    action {
                        launch {
                            println(Thread.currentThread().name)
                        }
                    }
                }
                button("Run - IO") {
                    action {
                        launch(Dispatchers.IO) {
                            println(Thread.currentThread().name)
                        }
                    }
                }
                //         jobs += launch(Executors.newCachedThreadPool().asCoroutineDispatcher())
                button("Run - Main") {
                    action {
                        launch(Dispatchers.Main) {
                            println(Thread.currentThread().name)
                        }
                    }
                }
                button("Run - unconstrained") {
                    action {
                        launch(Executors.newCachedThreadPool().asCoroutineDispatcher()) {
                            println(Thread.currentThread().name)
                        }
                    }
                }
                button("Run - JavaFX") {
                    action {
                        launch(Dispatchers.JavaFx) {
                            println(Thread.currentThread().name)
                        }
                    }
                }
                button("Run - Default") {
                    action {
                        launch(Dispatchers.Default) {
                            println(Thread.currentThread().name)
                        }
                    }
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