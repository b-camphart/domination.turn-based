package domination.usecases.battle

import domination.Culture
import domination.battle.Soldier

fun interface EndTurn {
    suspend fun endTurn(culture: Culture, output: Output)

    interface Output : UseCaseOutput {
        suspend fun turnEnded(result: Result)
    }

    class Result(
        val resetSoldiers: List<Soldier>
    )

}