package domination.fixtures.battle.soldier

import domination.fixtures.battle.BattleAssertions
import io.kotest.matchers.nulls.shouldNotBeNull

class SoldierTypeAssertions(val type: String) {

    infix fun somewhere_in(battleAssertions: BattleAssertions): SoldierAssertions {
        val soldier = battleAssertions.battle.soldiers.firstOrNull { it.type == type }
        soldier.shouldNotBeNull()
        return SoldierAssertions.Singular(soldier)
    }

}