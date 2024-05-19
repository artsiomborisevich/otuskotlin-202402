package dev.arborisevich.otuskotlin.kotlinwiz.common.exceptions

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizCommand

class UnknownQuizCommand(command: QuizCommand) : Throwable("Wrong command $command at mapping toTransport stage")
