package domination

import domination.battle.DominationBattle
import domination.battle.SoldierId
import domination.usecases.battle.AttackEstimate
import domination.usecases.battle.EstimateAttack
import domination.usecases.battle.battleContextContract
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull

class DominationBattleTest : FunSpec({
    val game = object : DominationBattle(mapOf(), Culture()) {
        public override var estimateAttack: EstimateAttack = super.estimateAttack
    }

    include(battleContextContract(game))

    test("attack estimate must be requested") {
        game.attackEstimate.shouldBeNull()
    }

    test("attack estimate is available after request") {
        game.estimateAttack = SuccessfullyEstimateAttackStub()
        game.estimateAttack(SoldierId())
        game.attackEstimate.shouldNotBeNull()
    }

    test("attack estimate is not available when the estimate fails") {
        game.estimateAttack = FailToEstimateAttackStub()
        game.estimateAttack(SoldierId())
        game.attackEstimate.shouldBeNull()
    }

}) {

    private class FailToEstimateAttackStub : EstimateAttack {
        override suspend fun createAttackEstimate(victimId: SoldierId): AttackEstimate? = null
    }

    private class SuccessfullyEstimateAttackStub : EstimateAttack {
        override suspend fun createAttackEstimate(victimId: SoldierId): AttackEstimate = AttackEstimate()
    }

}