package dev.arborisevich.otuskotlin.kotlinwiz.app.repo

import com.ninjasquad.springmockk.MockkBean
import dev.arborisevich.otuskotlin.kotlinwiz.app.config.QuestionConfig
import dev.arborisevich.otuskotlin.kotlinwiz.app.controllers.QuestionControllerV1
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLevel
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionFilterRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionIdRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IRepoQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.repo.QuestionRepoInMemory
import dev.arborisevich.otuskotlin.kotlinwiz.repo.QuestionRepoInitialized
import dev.arborisevich.otuskotlin.kotlinwiz.stubs.QuizQuestionStub
import io.mockk.coEvery
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(QuestionControllerV1::class, QuestionConfig::class)
internal class QuestionRepoInMemoryV1Test : QuestionRepoBaseV1Test() {

    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testRepo: IRepoQuestion

    @BeforeEach
    fun tearUp() {
        val slotAd = slot<DbQuestionRequest>()
        val slotId = slot<DbQuestionIdRequest>()
        val slotFl = slot<DbQuestionFilterRequest>()
        val repo = QuestionRepoInitialized(
            repo = QuestionRepoInMemory(randomUuid = { uuidNew }),
            initObjects = QuizQuestionStub.prepareSearchBeginnerList("", QuizQuestionLevel.BEGINNER)
        )
        coEvery { testRepo.createQuestion(capture(slotAd)) } coAnswers { repo.createQuestion(slotAd.captured) }
        coEvery { testRepo.readQuestion(capture(slotId)) } coAnswers { repo.readQuestion(slotId.captured) }
        coEvery { testRepo.updateQuestion(capture(slotAd)) } coAnswers { repo.updateQuestion(slotAd.captured) }
        coEvery { testRepo.deleteQuestion(capture(slotId)) } coAnswers { repo.deleteQuestion(slotId.captured) }
        coEvery { testRepo.searchQuestion(capture(slotFl)) } coAnswers { repo.searchQuestion(slotFl.captured) }
    }

    @Test
    override fun createQuestion() = super.createQuestion()

    @Test
    override fun readQuestion() = super.readQuestion()

    @Test
    override fun updateQuestion() = super.updateQuestion()

    @Test
    override fun deleteQuestion() = super.deleteQuestion()

    @Test
    override fun searchQuestion() = super.searchQuestion()

}
