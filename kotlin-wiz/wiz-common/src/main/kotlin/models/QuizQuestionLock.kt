package models

@JvmInline
value class QuizQuestionLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = models.QuizQuestionLock("")
    }
}
