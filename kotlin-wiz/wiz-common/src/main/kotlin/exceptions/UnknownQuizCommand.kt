package exceptions


class UnknownQuizCommand(command: models.QuizCommand) : Throwable("Wrong command $command at mapping toTransport stage")
