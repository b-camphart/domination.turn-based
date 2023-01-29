package domination.usecases.battle

import domination.battle.*

class AttackUseCase(
    private val battleContext: BattleContext,
    private val simulation: Attack.Simulation
) : Attack {

    override suspend fun attack(request: Attack.Request, output: Attack.Output) {
        val result = battleContext.updateBattle { battle, updateBattle ->
            val simulatedBattle: Battle = battle.simulateAttackOrNull(request, output)
                ?: return@updateBattle null
            updateBattle(simulatedBattle)
            result(simulatedBattle, request)
        }
        if (result != null) output.success(result)
    }

    private suspend fun Battle.simulateAttackOrNull(
        request: Attack.Request,
        output: UseCaseOutput
    ): Battle? {
        return with(simulation) {
            simulateAttack(request, output)
        }
    }

    private fun result(
        simulatedBattle: Battle,
        request: Attack.Request
    ) = Attack.Result(
        simulatedBattle.getSoldier(request.victimId)!!,
        simulatedBattle.getSoldier(request.attackerId)!!
    )

}
