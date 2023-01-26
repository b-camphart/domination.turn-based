package domination.usecases.battle

import domination.battle.SoldierId

interface EstimateAttack {
    suspend fun createAttackEstimate(victimId: SoldierId): AttackEstimate?
}