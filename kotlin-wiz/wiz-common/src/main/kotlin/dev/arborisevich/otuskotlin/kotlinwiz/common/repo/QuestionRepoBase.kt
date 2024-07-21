package dev.arborisevich.otuskotlin.kotlinwiz.common.repo

import dev.arborisevich.otuskotlin.kotlinwiz.common.helpers.errorSystem

abstract class QuestionRepoBase: IRepoQuestion {

    protected suspend fun tryQuestionMethod(block: suspend () -> IDbQuestionResponse) = try {
        block()
    } catch (e: Throwable) {
        DbQuestionResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryQuestionsMethod(block: suspend () -> IDbQuestionsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbQuestionsResponseErr(errorSystem("methodException", e = e))
    }

}
