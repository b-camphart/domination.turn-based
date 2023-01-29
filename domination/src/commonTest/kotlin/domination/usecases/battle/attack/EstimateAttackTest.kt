package domination.usecases.battle.attack

import domination.Culture
import domination.battle.Agent
import domination.battle.Battle
import domination.battle.SoldierId
import domination.entities.soldier.nonDefaultSoldier
import domination.usecases.battle.Attack
import domination.usecases.battle.AttackEstimate
import domination.usecases.battle.EstimateAttackUseCase
import domination.usecases.battle.FakeBattleContext
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs

class EstimateAttackTest : FunSpec() {

    init {
        context("attack simulation fails") {
            val simulation = FailingAttackSimulationDummy(FakeSimulationError())

            test("simulation errors are collected in output") {
                EstimateAttackUseCase(battleContext, simulation = simulation)
                    .estimateAttack(Attack.Request(Agent(Culture()), SoldierId(), SoldierId()), output = outputSpy)

                outputSpy.validationErrors.find { it is FakeSimulationError }
                    .shouldNotBeNull()
            }

            test("battle is not updated") {
                val battleBefore = battleContext.getBattle()
                EstimateAttackUseCase(battleContext, simulation = simulation)
                    .estimateAttack(Attack.Request(Agent(Culture()), SoldierId(), SoldierId()), output = outputSpy)

                battleContext.getBattle().shouldBeSameInstanceAs(battleBefore)
            }
        }

        context("attack simulation succeeds") {
            val newVictimInBattle = nonDefaultSoldier()
            val newAttackerInBattle = nonDefaultSoldier()
            val victimId = newVictimInBattle.id
            val attackerId = newAttackerInBattle.id
            val simulation = SucceedingAttackSimulationDummy(Battle(soldiers = listOf(newVictimInBattle, newAttackerInBattle)))

            test("battle is not updated") {
                val battleBefore = battleContext.getBattle()
                EstimateAttackUseCase(battleContext, simulation = simulation)
                    .estimateAttack(Attack.Request(Agent(Culture()), attackerId, victimId), output = outputSpy)

                battleContext.getBattle().shouldBeSameInstanceAs(battleBefore)
            }

            test("outputs new state of victim") {
                EstimateAttackUseCase(battleContext, simulation = simulation)
                    .estimateAttack(Attack.Request(Agent(Culture()), attackerId, victimId), output = outputSpy)

                outputSpy.result!!.estimatedHealthOf(newVictimInBattle.id).shouldBe(newVictimInBattle.health.value)
            }

            test("outputs new state of attacker") {
                EstimateAttackUseCase(battleContext, simulation = simulation)
                    .estimateAttack(Attack.Request(Agent(Culture()), attackerId, victimId), output = outputSpy)

                outputSpy.result!!.estimatedHealthOf(newAttackerInBattle.id).shouldBe(newAttackerInBattle.health.value)
            }
        }
    }

    private val battleContext = FakeBattleContext()
    private val outputSpy = OutputSpy()

    private class OutputSpy : Attack.Estimate.Output {
        val validationErrors = mutableListOf<Throwable>()
        var result: AttackEstimate? = null
            private set
        override suspend fun validationFailed(failure: Throwable) {
            validationErrors.add(failure)
        }

        override suspend fun presentEstimate(estimate: AttackEstimate) {
            result = estimate
        }
    }

}