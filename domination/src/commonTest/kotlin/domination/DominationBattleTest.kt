package domination

import domination.battle.Agent
import domination.battle.DominationBattle
import domination.battle.SoldierId
import domination.usecases.battle.Attack
import domination.usecases.battle.AttackEstimate
import domination.usecases.battle.attack.FakeSimulationError
import domination.usecases.battle.battleContextContract
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull

class DominationBattleTest : FunSpec({
    val game = object : DominationBattle(mapOf(), Culture()) {
        public override var estimateAttack: Attack.Estimate = super.estimateAttack
    }

    include(battleContextContract(game))

    test("attack estimate must be requested") {
        game.attackEstimate.shouldBeNull()
    }

    test("attack estimate is available after request") {
        game.estimateAttack = SuccessfullyEstimateAttackStub()
        game.estimateAttack(Attack.Request(Agent(Culture()), SoldierId(), SoldierId()))
        game.attackEstimate.shouldNotBeNull()
    }

    test("attack estimate is not available when the estimate fails") {
        game.estimateAttack = FailToEstimateAttack_Stub()
        try {
            game.estimateAttack(Attack.Request(Agent(Culture()), SoldierId(), SoldierId()))
        } catch (_: Throwable) {}
        game.attackEstimate.shouldBeNull()
    }

    test("all failures should be displayed") {
        game.estimateAttack = FailForMultipleReasonsToEstimateAttack_Dummy(3)
        val failure = shouldThrowAny {
            game.estimateAttack(Attack.Request(Agent(Culture()), SoldierId(), SoldierId()))
        }
        failure.suppressedExceptions.shouldHaveSize(3)
    }

}) {

    private class FailToEstimateAttack_Stub : Attack.Estimate {
        override suspend fun estimateAttack(request: Attack.Request, output: Attack.Estimate.Output) {
            output.validationFailed(FakeSimulationError())
        }
    }

    private class FailForMultipleReasonsToEstimateAttack_Dummy(private val failureCount: Int) : Attack.Estimate {
        override suspend fun estimateAttack(request: Attack.Request, output: Attack.Estimate.Output) {
            repeat(failureCount) {
                output.validationFailed(FakeSimulationError())
            }
        }
    }

    private class SuccessfullyEstimateAttackStub : Attack.Estimate {
        override suspend fun estimateAttack(request: Attack.Request, output: Attack.Estimate.Output) {
            output.presentEstimate(AttackEstimate())
        }
    }

}