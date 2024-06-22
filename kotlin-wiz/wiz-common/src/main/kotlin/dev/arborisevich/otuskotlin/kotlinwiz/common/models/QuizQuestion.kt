package dev.arborisevich.otuskotlin.kotlinwiz.common.models

data class QuizQuestion(
    var id: QuizQuestionId = QuizQuestionId.NONE,

    var text: String = "",
    var answerOptions: List<QuizAnswerOption> = mutableListOf(),
    var answer: String = "",
    var explanation: String = "",
    var level: QuizQuestionLevel = QuizQuestionLevel.ADVANCED,

    var userId: QuizUserId = QuizUserId.NONE,
    var lock: QuizQuestionLock = QuizQuestionLock.NONE,
    val permissionsClient: MutableSet<QuizQuestionPermissionsClient> = mutableSetOf()
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = QuizQuestion()
    }

    fun deepCopy(): QuizQuestion = copy(
        answerOptions = answerOptions.toMutableList(),
        permissionsClient = permissionsClient.toMutableSet(),
    )

}
