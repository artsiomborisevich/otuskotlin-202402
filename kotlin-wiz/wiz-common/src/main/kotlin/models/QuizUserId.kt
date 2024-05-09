package models

@JvmInline
value class QuizUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = models.QuizUserId("")
    }
}
