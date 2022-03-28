package com.knowlegespike.fxclient

import javafx.geometry.Pos.CENTER
import javafx.geometry.Pos.CENTER_LEFT
import javafx.scene.Cursor.HAND
import javafx.scene.paint.Color
import javafx.scene.paint.Color.*
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.text.FontWeight.BOLD
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        // Containers
        val rowWrapper by cssclass()
        val contentWrapper by cssclass()
        val content by cssclass()
        val topbar by cssclass()
        val head by cssclass()
        val statsbar by cssclass()
        val codeview by cssclass()
        val issuelist by cssclass()
        val mainScreen by cssclass()
        val stat by cssclass()
        val hContainer by cssclass()
        val linkLook by cssclass()
        val h1 by cssclass()
        val h2 by cssclass()

        // Dimensions
        val pageWidth = 980.px

        // Colors
        val lightBackgroundColor = c("#fafafa")
        val darkBackgroundColor = c("#f5f5f5")
        val linkColor = c("#4078c0")
        val contrastColor = c("#d26911")
        val borderLineColor = c("#e5e5e5")
        val darkTextColor = c("#666")

        // Buttons
        val successButton by cssclass()

        // Default looks
        val lightBackground by cssclass()
        val whiteBackground by cssclass()
        val defaultContentPadding by cssclass()
        val defaultSpacing by cssclass()
        val bold by cssclass()
        val black by cssclass()

        // Icons
        val icon by cssclass()
        val small by cssclass()
        val medium by cssclass()
        val large by cssclass()
        val logoIcon by cssclass()
        val repoIcon by cssclass()
        val codeIcon by cssclass()
        val issuesIcon by cssclass()
        val pullRequestsIcon by cssclass()
        val settingsIcon by cssclass()
        val historyIcon by cssclass()
        val branchIcon by cssclass()
        val releasesIcon by cssclass()
        val contributorsIcon by cssclass()
        val commentIcon by cssclass()
        val openIssueIcon by cssclass()
        val crossIcon by cssclass()
        val starIcon by cssclass()
        val locationIcon by cssclass()
        val linkIcon by cssclass()
        val clockIcon by cssclass()
    }

    init {
        root {
            unsafe("-fx-accent", linkColor)
            unsafe("-fx-faint-focus-color", raw("transparent"))
            unsafe("-fx-text-background-color", raw("ladder( -fx-background, -fx-dark-text-color 46%, -fx-dark-text-color 59%, -fx-mid-text-color 60% )"))
        }

        rowWrapper {
            alignment = CENTER
        }

        rowWrapper child star {
            alignment = CENTER
        }

        linkLook {
            fill = linkColor
            textFill = linkColor
        }

        contentWrapper {
            minWidth = pageWidth
            maxWidth = minWidth
            alignment = CENTER
        }

        content {
            minWidth = pageWidth
            maxWidth = minWidth
            alignment = CENTER_LEFT
        }

        defaultContentPadding {
            padding = box(15.px)
        }

        defaultSpacing {
            spacing = 10.px
        }

        lightBackground {
            backgroundColor += lightBackgroundColor
        }

        whiteBackground {
            backgroundColor += Color.WHITE
        }

        bold {
            fontWeight = BOLD
        }

        black {
            textFill = BLACK
        }

        codeview child contentWrapper {
            spacing = 10.px
        }


        mainScreen {
            backgroundColor += WHITE
            padding = box(25.px)
            fontSize = 20.px
            fontWeight = BOLD

        }

        statsbar {
            borderColor += box(TRANSPARENT, TRANSPARENT, contrastColor, TRANSPARENT)
            borderWidth += box(0.px, 0.px, 8.px, 0.px)

            borderColor += box(borderLineColor)
            borderWidth += box(1.px)

            label {
                textFill = darkTextColor
            }

            stat {
                spacing = 3.px
                padding = box(14.px, 0.px)
                alignment = CENTER
            }
        }

        topbar {
            backgroundColor += darkBackgroundColor
            padding = box(10.px, 0.px)
            content {
                spacing = 20.px
            }
            borderColor += box(TRANSPARENT, TRANSPARENT, borderLineColor, TRANSPARENT)
            label {
                fontWeight = BOLD
                fontSize = 16.px
            }
        }

        hContainer {
            spacing = 6.px
            padding = box(0.px, 0.px)
            alignment = CENTER_LEFT
        }

        h1 {
            fontSize = 22.px
        }

        h2 {
            fontSize = 18.px
            textFill = darkTextColor
        }

        head {
            padding = box(20.px, 0.px)
            spacing = 20.px
            backgroundColor += lightBackgroundColor
            label {
                fontWeight = BOLD
                fontSize = 18.px
            }
        }

        s(listView, tableView) {
            s(hover, selected) {
                backgroundColor += darkBackgroundColor
            }
            focusColor = TRANSPARENT
            faintFocusColor = TRANSPARENT
            and(focused) {
                unsafe("-fx-background-color", raw("-fx-box-border, -fx-control-inner-background"))
                backgroundInsets = multi(box(0.px), box(1.px))
                padding = box(1.px)
            }
        }

        listCell and odd {
            unsafe("-fx-background", raw("-fx-control-inner-background"))
        }

        tabPane {
            prefWidth = pageWidth
            tabHeaderBackground {
                backgroundColor += lightBackgroundColor
                borderColor += box(TRANSPARENT, TRANSPARENT, borderLineColor, TRANSPARENT)
            }
            tab {
                backgroundColor += TRANSPARENT
                textFill = darkTextColor
                padding = box(7.px, 11.px)
            }
            tab and selected {
                backgroundColor += WHITE
                borderColor += box(contrastColor, TRANSPARENT, TRANSPARENT, TRANSPARENT)
                borderColor += box(TRANSPARENT, borderLineColor)
                borderColor += box(TRANSPARENT)
                borderWidth += box(3.px)
                borderWidth += box(1.px)
                borderWidth += box(1.px)
                borderRadius += box(3.px, 3.px, 0.px, 0.px)
                focusColor = TRANSPARENT
                faintFocusColor = TRANSPARENT
            }
        }

        issuelist contains icon {
            translateY = 3.px
        }

        issuelist contains listCell {
            padding = box(10.px)
        }

        scrollBar {
            padding = box(0.px)
            prefWidth = 12.px
            prefHeight = 12.px
            track {
                backgroundColor += c("#f3f3f3")
            }
            thumb {
                borderColor += box(c("#bbb"))
                backgroundColor += c("#f3f3f3")
            }
            and(hover, pressed) {
                thumb {
                    backgroundColor += c("#757575")
                    borderColor += box(c("#757575"))
                    backgroundInsets += box(0.px)
                }
            }
        }

        // Buttons
        successButton {
            borderRadius += box(4.px)
            padding = box(8.px, 15.px)
            backgroundInsets += box(0.px)
            borderColor += box(c("#5ca941"))
            textFill = Color.WHITE
            fontWeight = BOLD
            backgroundColor += LinearGradient(0.0, 0.0, 0.0, 1.0, true, CycleMethod.NO_CYCLE, Stop(0.0, c("#8add6d")), Stop(1.0, c("#60b044")))
            and(hover) {
                backgroundColor += LinearGradient(0.0, 0.0, 0.0, 1.0, true, CycleMethod.NO_CYCLE, Stop(0.0, c("#79d858")), Stop(1.0, c("#569e3d")))
            }
            and(pressed) {
                backgroundColor += c("#569e3d")
            }
            icon {
                backgroundColor += WHITE
            }
        }

        // Icons
        icon {
            minWidth = 16.px
            maxWidth = 16.px
            minHeight = 16.px
            maxHeight = 16.px
            backgroundColor += GRAY
            and(small) {
                minWidth = 12.px
                maxWidth = 12.px
                minHeight = 12.px
                maxHeight = 12.px
            }
            and(medium) {
                minWidth = 28.px
                maxWidth = 28.px
                minHeight = 28.px
                maxHeight = 28.px
            }
            and(large) {
                minWidth = 48.px
                maxWidth = 48.px
                minHeight = 48.px
                maxHeight = 48.px
            }
        }

        hyperlink {
            textFill = linkColor
            padding = box(0.px)
            s(armed, visited, hover and armed) {
                unsafe("-fx-text-fill", raw("-fx-accent"))
            }
        }

    }
}
