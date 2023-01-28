package domination.usecases.battle.attack

import domination.battle.Battle
import domination.usecases.battle.Attack
import domination.usecases.battle.UseCaseOutput

class SucceedingAttackSimulationDummy(
    private val returnBattle: Battle
) : Attack.Simulation {
    override suspend fun Battle.simulateAttack(request: Attack.Request, errorCollector: UseCaseOutput): Battle {
        return returnBattle
    }
}