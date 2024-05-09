
import kotlinx.datetime.Instant
import models.QuizCommand
import models.QuizError
import models.QuizQuestion
import models.QuizQuestionFilter
import models.QuizRequestId
import models.QuizState
import models.QuizWorkMode
import stubs.QuizStubs

data class QuizContext(
    var command: QuizCommand = QuizCommand.NONE,
    var state: QuizState = QuizState.NONE,
    val errors: MutableList<QuizError> = mutableListOf(),

    var workMode: QuizWorkMode = QuizWorkMode.PROD,
    var stubCase: QuizStubs = QuizStubs.NONE,

    var requestId: QuizRequestId = QuizRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var questionRequest: QuizQuestion = QuizQuestion(),
    var questionFilterRequest: QuizQuestionFilter = QuizQuestionFilter(),

    var questionResponse: QuizQuestion = QuizQuestion(),
    var questionsResponse: MutableList<QuizQuestion> = mutableListOf(),

    )