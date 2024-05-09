package models

@JvmInline
value class QuizQuestionId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = QuizQuestionId("")
    }
}
