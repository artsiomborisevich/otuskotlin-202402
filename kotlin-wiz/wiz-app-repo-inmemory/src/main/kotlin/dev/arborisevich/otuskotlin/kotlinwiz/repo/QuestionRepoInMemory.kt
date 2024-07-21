package dev.arborisevich.otuskotlin.kotlinwiz.repo

import com.benasher44.uuid.uuid4
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionId
import dev.arborisevich.otuskotlin.kotlinwiz.common.models.QuizQuestionLock
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionFilterRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionIdRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionRequest
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.DbQuestionsResponseOk
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IDbQuestionResponse
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IDbQuestionsResponse
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.IRepoQuestion
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.QuestionRepoBase
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.errorDb
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.errorEmptyId
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.errorEmptyLock
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.errorNotFound
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.errorRepoConcurrency
import dev.arborisevich.otuskotlin.kotlinwiz.common.repo.exceptions.RepoEmptyLockException
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class QuestionRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : QuestionRepoBase(), IRepoQuestion, IRepoQuestionInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, QuestionEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(questions: Collection<QuizQuestion>) = questions.map { question ->
        val entity = QuestionEntity(question)
        require(entity.id != null)
        cache.put(entity.id, entity)
        question
    }

    override suspend fun createQuestion(rq: DbQuestionRequest): IDbQuestionResponse = tryQuestionMethod {
        val key = randomUuid()
        val question = rq.question.copy(id = QuizQuestionId(key), lock = QuizQuestionLock(randomUuid()))
        val entity = QuestionEntity(question)

        mutex.withLock {
            cache.put(key, entity)
        }

        DbQuestionResponseOk(question)
    }

    override suspend fun readQuestion(rq: DbQuestionIdRequest): IDbQuestionResponse = tryQuestionMethod {
        val key = rq.id.takeIf { it != QuizQuestionId.NONE }?.asString() ?: return@tryQuestionMethod errorEmptyId

        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbQuestionResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateQuestion(rq: DbQuestionRequest): IDbQuestionResponse = tryQuestionMethod {
        val rqQuestion = rq.question
        val id = rqQuestion.id.takeIf { it != QuizQuestionId.NONE } ?: return@tryQuestionMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqQuestion.lock.takeIf { it != QuizQuestionLock.NONE } ?:  return@tryQuestionMethod errorEmptyLock(id)

        mutex.withLock {
            val oldQuestion = cache.get(key)?.toInternal()
            when {
                oldQuestion == null -> errorNotFound(id)
                oldQuestion.lock == QuizQuestionLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldQuestion.lock != oldLock -> errorRepoConcurrency(oldQuestion, oldLock)
                else -> {
                    val newQuestion = rqQuestion.copy(lock = QuizQuestionLock(randomUuid()))
                    val entity = QuestionEntity(newQuestion)
                    cache.put(key, entity)
                    DbQuestionResponseOk(newQuestion)
                }
            }
        }
    }


    override suspend fun deleteQuestion(rq: DbQuestionIdRequest): IDbQuestionResponse = tryQuestionMethod {
        val id = rq.id.takeIf { it != QuizQuestionId.NONE } ?: return@tryQuestionMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != QuizQuestionLock.NONE } ?:  return@tryQuestionMethod errorEmptyLock(id)

        mutex.withLock {
            val oldQuestion = cache.get(key)?.toInternal()
            when {
                oldQuestion == null -> errorNotFound(id)
                oldQuestion.lock == QuizQuestionLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldQuestion.lock != oldLock -> errorRepoConcurrency(oldQuestion, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbQuestionResponseOk(oldQuestion)
                }
            }
        }
    }

    /**
     * Search questions by filter
     * If any of the parameters is not set in the filter, filtering is not carried out on it
     */
    override suspend fun searchQuestion(rq: DbQuestionFilterRequest): IDbQuestionsResponse = tryQuestionsMethod {
        val result: List<QuizQuestion> = cache.asMap().asSequence()
            .filter { entry ->
                rq.textFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.text?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbQuestionsResponseOk(result)
    }
}
