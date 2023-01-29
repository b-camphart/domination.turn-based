package domination.usecases.battle.attack

import domination.Culture
import domination.battle.Agent
import domination.battle.Battle
import domination.battle.SoldierId
import domination.entities.soldier.defaultSoldier
import domination.usecases.battle.Attack
import domination.usecases.battle.AttackUseCase
import domination.usecases.battle.FakeBattleContext
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs

class AttackTest : FunSpec() {

    init {
        context("attack simulation fails") {
            val simulation = FailingAttackSimulationDummy(FakeSimulationError())

            test("simulation errors are collected in output") {
                AttackUseCase(battleContext, simulation = simulation)
                    .attack(Attack.Request(Agent(Culture()), SoldierId(), SoldierId()), output = outputSpy)

                outputSpy.validationErrors.find { it is FakeSimulationError }
                    .shouldNotBeNull()
            }

            test("no output is produced") {
                AttackUseCase(battleContext, simulation = simulation)
                    .attack(Attack.Request(Agent(Culture()), SoldierId(), SoldierId()), output = outputSpy)

                outputSpy.result.shouldBeNull()
            }

            test("battle is not updated") {
                val contextSpy = BattleContextSpy()
                AttackUseCase(contextSpy, simulation = simulation)
                    .attack(Attack.Request(Agent(Culture()), SoldierId(), SoldierId()), output = outputSpy)

                contextSpy.spiedUpdate.shouldBeNull()
            }
        }

        context("attack simulation succeeds") {
            val expectedBattle = Battle(soldiers = List(2) { defaultSoldier() })
            val simulation = SucceedingAttackSimulationDummy(expectedBattle)

            test("updates battle") {
                AttackUseCase(
                    battleContext,
                    simulation = simulation
                )
                    .attack(Attack.Request(Agent(Culture()), expectedBattle.soldiers[0].id, expectedBattle.soldiers[1].id), output = outputSpy)

                battleContext.getBattle().shouldBeSameInstanceAs(expectedBattle)
            }

            test("output is produced") {
                AttackUseCase(
                    battleContext,
                    simulation = simulation
                )
                    .attack(Attack.Request(Agent(Culture()), expectedBattle.soldiers[0].id, expectedBattle.soldiers[1].id), output = outputSpy)

                outputSpy.result.shouldNotBeNull().should {
                    it.attacker.id.shouldBe(expectedBattle.soldiers[0].id)
                    it.victim.id.shouldBe(expectedBattle.soldiers[1].id)
                }
            }

        }

    }

    private val battleContext = FakeBattleContext()
    private val outputSpy = OutputSpy()

    private class OutputSpy : Attack.Output {
        val validationErrors = mutableListOf<Throwable>()
        var result: Attack.Result? = null
            private set
        override suspend fun validationFailed(failure: Throwable) {
            validationErrors.add(failure)
        }

        override suspend fun success(result: Attack.Result) {
            this.result = result
        }
    }

}