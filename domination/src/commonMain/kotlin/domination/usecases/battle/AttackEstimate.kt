package domination.usecases.battle

import domination.battle.Soldier
import domination.battle.SoldierId

class AttackEstimate(
    private val soldier1: Soldier,
    private val soldier2: Soldier
) {
    fun estimatedHealthOf(soldierId: SoldierId): Int? {
        return soldier1.takeIf { it.id == soldierId }?.health?.value
            ?: soldier2.takeIf { it.id == soldierId }?.health?.value
    }

}
