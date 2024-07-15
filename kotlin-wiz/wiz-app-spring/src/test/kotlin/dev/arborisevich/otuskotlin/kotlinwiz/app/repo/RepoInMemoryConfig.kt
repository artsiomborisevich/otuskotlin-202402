package dev.arborisevich.otuskotlin.kotlinwiz.app.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IRepoQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.repo.QuestionRepoInMemory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class RepoInMemoryConfig {

    @Bean
    @Primary
    fun prodRepo(): IRepoQuestion = QuestionRepoInMemory()
}
