package dev.arborisevich.otuskotlin.kotlinwiz.repo.postgres

import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

fun Table.levelEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.LEVEL_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.LEVEL_TYPE_BEGINNER -> QuizQuestionLevel.BEGINNER
            SqlFields.LEVEL_TYPE_ADVANCED -> QuizQuestionLevel.ADVANCED
            SqlFields.LEVEL_TYPE_EXPERT -> QuizQuestionLevel.EXPERT
            else -> QuizQuestionLevel.NONE
        }
    },
    toDb = { value ->
        when (value) {
            QuizQuestionLevel.BEGINNER -> PgLevelBeginner
            QuizQuestionLevel.ADVANCED -> PgLevelAdvanced
            QuizQuestionLevel.EXPERT -> PgLevelExpert
            QuizQuestionLevel.NONE -> throw Exception("Wrong value of QuizQuestionLevel. NONE is unsupported")
        }
    }
)

sealed class PgLevelValue(eValue: String) : PGobject() {
    init {
        type = SqlFields.LEVEL_TYPE
        value = eValue
    }
}

object PgLevelExpert : PgLevelValue(SqlFields.LEVEL_TYPE_EXPERT) {
    private fun readResolve(): Any = PgLevelExpert
}

object PgLevelBeginner : PgLevelValue(SqlFields.LEVEL_TYPE_BEGINNER) {
    private fun readResolve(): Any = PgLevelBeginner
}

object PgLevelAdvanced : PgLevelValue(SqlFields.LEVEL_TYPE_ADVANCED) {
    private fun readResolve(): Any = PgLevelAdvanced
}
