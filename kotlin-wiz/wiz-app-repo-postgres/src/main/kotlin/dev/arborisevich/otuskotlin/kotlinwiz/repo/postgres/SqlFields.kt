package dev.arborisevich.otuskotlin.kotlinwiz.repo.postgres

object SqlFields {
    const val QUESTION_ID = "question_id"
    const val QUESTION_TEXT = "question"
    const val LEVEL = "level"
    const val ANSWER = "correct_answer"
    const val EXPLANATION = "explanation"
    const val LOCK = "lock"
    const val LOCK_OLD = "lock_old"

    const val LEVEL_TYPE = "quiz_level_type"
    const val LEVEL_TYPE_BEGINNER = "BEGINNER"
    const val LEVEL_TYPE_ADVANCED = "ADVANCED"
    const val LEVEL_TYPE_EXPERT = "EXPERT"

    const val FILTER_TEXT = QUESTION_TEXT

    const val DELETE_OK = "DELETE_OK"

    fun String.quoted() = "\"$this\""

    const val ANSWER_OPTION_ID = "answer_option_id"
//    const val OPTION_GROUP_ID = "option_group_id"
    const val OPTION_ID = "option_id"
    const val OPTION_TEXT = "option_text"

    val allFields = listOf(
        QUESTION_ID, QUESTION_TEXT, LEVEL, ANSWER, EXPLANATION, LOCK,
    )
}
