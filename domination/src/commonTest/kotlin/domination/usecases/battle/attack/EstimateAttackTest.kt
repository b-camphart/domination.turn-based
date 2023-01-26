package domination.usecases.battle.attack

import domination.battle.Soldier
import domination.battle.SoldierId
import domination.entities.soldier.defaultSoldier
import domination.fixtures.meleeAttack
import domination.usecases.battle.EstimateAttack
import domination.usecases.battle.EstimateAttackUseCase
import domination.usecases.battle.FakeBattleContext
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class EstimateAttackTest : FunSpec() {

    init {
        context("victim must be in the game") {
            test("estimate fails without the victim in the game") {
                estimateAttack.createAttackEstimate(SoldierId())
                    .shouldBeNull()
            }
        }
        test("victim receives damage") {
            givenSomeSoldier()
                .withAttack(meleeAttack(of_strength = 10))

            val victim = givenSomeSoldier()
                .withHealth(10)

            estimateAttack.createAttackEstimate(victim.id)
                .shouldNotBeNull()
                .estimatedHealthOf(victim.id).shouldBe(1)
        }
    }

    private val context = FakeBattleContext()

    private val estimateAttack: EstimateAttack by lazy { EstimateAttackUseCase(context) }

    private fun givenSomeSoldier(): Soldier {
        val soldier = defaultSoldier()
        context.soldiers.add(soldier)
        return soldier
    }

}