package domination.usecases.battle

fun interface UseCaseOutput {
    suspend fun validationFailed(failure: Throwable)
}