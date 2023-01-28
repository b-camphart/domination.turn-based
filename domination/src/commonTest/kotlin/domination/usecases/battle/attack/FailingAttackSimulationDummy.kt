package domination.usecases.battle.attack

import domination.battle.Battle
import domination.usecases.battle.Attack
import domination.usecases.battle.UseCaseOutput

class FailingAttackSimulationDummy(
    private val throwable: Throwable
) : Attack.Simulation {
    override suspend fun Battle.simulateAttack(request: Attack.Request, errorCollector: UseCaseOutput): Battle? {
        errorCollector.validationFailed(throwable)
        return null
    }
}