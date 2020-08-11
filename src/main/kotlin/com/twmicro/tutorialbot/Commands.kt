package com.twmicro.tutorialbot

object Commands {
    val commands = ArrayList<IBotCommand>()

    val testCommand: IBotCommand = command(object: IBotCommand{
        override fun getName(): String {
            return "/тест"
        }

        override fun execute(peerId:Int?, args: String) {
            vk.messages().send(actor).message("Тест успешен!").peerId(peerId).randomId(random.nextInt(999999)).execute()
        }

    })
    private fun command(command: IBotCommand) : IBotCommand {
        commands.add(command)
        return command
    }
}