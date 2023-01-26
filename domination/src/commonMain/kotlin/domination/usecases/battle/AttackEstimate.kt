package domination.usecases.battle

import domination.battle.Soldier
import domination.battle.SoldierId

class AttackEstimate(private val soldier: Soldier? = null) {
    fun estimatedHealthOf(soldierId: SoldierId): Int? {
        return soldier?.takeIf { it.id == soldierId }?.health?.value
    }

}
