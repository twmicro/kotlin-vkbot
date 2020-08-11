package com.twmicro.tutorialbot

interface IBotCommand {
    fun getName() : String
    fun execute(peerId: Int?, args: String)
}