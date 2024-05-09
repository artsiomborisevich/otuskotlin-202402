package models

@JvmInline
value class QuizRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = models.QuizRequestId("")
    }
}
