package domination.usecases.battle

import domination.Culture
import domination.battle.Battle
import domination.battle.BattleContext
import domination.battle.Soldier

class EndTurnUseCase(
    private val context: BattleContext
) : EndTurn {

    override suspend fun endTurn(culture: Culture, output: EndTurn.Output) {
        val result = context.updateBattle { battle, updateBattle ->
            val updatedSoldiers = battle.getSoldiersAlliedWithOrNull(culture, output)
                ?.map { it.withoutAttackExpended() }
                ?: return@updateBattle null
            updateBattle(battle.withUpdatedSoldiers(updatedSoldiers))
            EndTurn.Result(updatedSoldiers)
        } ?: return

        output.turnEnded(result)
    }

    private suspend fun Battle.getSoldiersAlliedWithOrNull(culture: Culture, output: EndTurn.Output): List<Soldier>? {
        return try {
            getSoldiersAlliedWith(culture)
        } catch (t: Throwable) {
            output.validationFailed(t)
            null
        }
    }

    private fun Battle.getSoldiersAlliedWith(culture: Culture): List<Soldier> {
        if (isOver) throw BattleHasEnded()
        val alliedSoldiers = soldiers.filter { it.culture == culture }
        if (alliedSoldiers.isEmpty()) throw CultureNotInBattle()
        if (alliedSoldiers.count { !it.isDead } == 0) throw CultureHasLost()
        return alliedSoldiers
    }

    private fun Battle.withUpdatedSoldiers(
        updatedSoldiers: List<Soldier>
    ) = updatedSoldiers.fold(this) { battle, soldier -> battle.withSoldier(soldier) }

}