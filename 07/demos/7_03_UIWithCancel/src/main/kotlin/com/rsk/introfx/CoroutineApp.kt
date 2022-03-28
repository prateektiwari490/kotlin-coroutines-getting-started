package com.knowledgespike.introfx

import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import kotlinx.coroutines.*
import tornadofx.*
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.javafx.JavaFx as UI

fun main(args: Array<String>) = launch<CoroutineApp>(args)


class CoroutineApp : App(IntroView::class) {
    override fun start(stage: Stage) {
        stage.width = 200.0
        stage.height = 400.0
        super.start(stage)
    }
}

class IntroView : View(), CoroutineScope {
    override val root = BorderPane()
    val counter = SimpleIntegerProperty()

    init {
        title = "Counter"

        with(root) {
            style {
                padding = box(20.px)
            }

            center {
                vbox(10.0) {
                    alignment = Pos.CENTER

                    label() {
                        bind(counter)
                        style { fontSize = 25.px }
                    }

                    button("Click to increment") {
                        setOnAction {
                            // 2. add launch coroutine
                            launch(Dispatchers.UI) {
                                increment()
                            }
                        }
                    }
                    button("Click to cancel") {

                    }.setOnAction {
                        // 2. add launch coroutine
                        job.cancel()
                        job = Job()
                    }
                }
            }
        }
    }

    fun oldincrement() {
        Thread.sleep(3000)
        launch(Dispatchers.UI) {
            counter.value += 1
        }
    }

    suspend fun increment() {
        while(true) {
            delay(1000)

            counter.value += 1
        }

    }

    override fun onDelete() {
        job.cancel()
    }

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
}
