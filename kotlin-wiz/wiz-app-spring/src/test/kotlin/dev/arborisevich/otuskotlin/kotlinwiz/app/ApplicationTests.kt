package dev.arborisevich.otuskotlin.kotlinwiz.app

import dev.arborisevich.otuskotlin.kotlinwiz.app.config.QuestionConfigPostgres
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTests {
    @Autowired
    var pgConf: QuestionConfigPostgres = QuestionConfigPostgres()

    @Test
    fun contextLoads() {
        assertEquals(5433, pgConf.psql.port)
        assertEquals("quiz-questions", pgConf.psql.database)
    }
}
