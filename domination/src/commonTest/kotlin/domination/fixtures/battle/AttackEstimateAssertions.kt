package domination.fixtures.battle

import domination.battle.SoldierHealth
import domination.fixtures.battle.soldier.SoldierAssertions
import domination.usecases.battle.AttackEstimate
import io.kotest.matchers.shouldBe

class AttackEstimateAssertions(val estimate: AttackEstimate) {

    fun should_show(
        soldierAssertions: SoldierAssertions.Singular,
        at: SoldierHealth
    ): AttackEstimateAssertions {
        estimate.estimatedHealthOf(soldierAssertions.soldier.id)
            .shouldBe(at.value)
        return this
    }

}