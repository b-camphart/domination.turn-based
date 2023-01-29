package domination.usecases.battle

import domination.battle.BattleAccess

class EstimateAttackUseCase(
    private val access: BattleAccess,
    private val simulation: Attack.Simulation
) : Attack.Estimate {

    override suspend fun estimateAttack(request: Attack.Request, output: Attack.Estimate.Output) {
        val battle = with(simulation) {
            access.getBattle().simulateAttack(request, output)
        } ?: return
        val estimate = AttackEstimate(
            battle.getSoldier(request.victimId)!!,
            battle.getSoldier(request.attackerId)!!
        )
        output.presentEstimate(estimate)
    }
}