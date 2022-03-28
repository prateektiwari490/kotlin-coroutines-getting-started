package com.knowlegespike.fxclient

import com.knowlegespike.fxclient.views.MainView
import javafx.stage.Stage
import tornadofx.App
import tornadofx.InternalWindow
import tornadofx.launch

fun main(args: Array<String>) = launch<EchoClient>(args)


class EchoClient : App(MainView::class, InternalWindow.Styles::class) {
    override fun start(stage: Stage) {
        stage.width = 500.0
        stage.height = 500.0
        super.start(stage)
    }

}

