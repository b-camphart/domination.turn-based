package domination.usecases.battle

import domination.battle.BattleAccess
import domination.battle.SoldierId

class EstimateAttackUseCase(
    private val access: BattleAccess
) : EstimateAttack {
    override suspend fun createAttackEstimate(victimId: SoldierId): AttackEstimate? {
        val battle = access.getBattle()
        val victim = battle.getSoldier(victimId) ?: return null
        return AttackEstimate(victim.withHealth(victim.health.value / 10))
    }
}