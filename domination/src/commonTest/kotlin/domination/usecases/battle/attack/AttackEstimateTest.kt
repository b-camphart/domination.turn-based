package domination.usecases.battle.attack

import domination.battle.SoldierId
import domination.entities.soldier.defaultSoldier
import domination.fixtures.health
import domination.usecases.battle.AttackEstimate
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class AttackEstimateTest : FunSpec({

    context("getting the estimated health of a soldier in the estimate") {

        test("cannot get estimated health of soldier not in estimate") {
            AttackEstimate(defaultSoldier(), defaultSoldier())
                .estimatedHealthOf(SoldierId())
                .shouldBeNull()
        }

        test("can get estimated health of soldier in estimate") {
            val soldier = defaultSoldier(health = 15.health)
            AttackEstimate(soldier, defaultSoldier())
                .estimatedHealthOf(soldier.id)
                .shouldBe(15)

            AttackEstimate(defaultSoldier(), soldier)
                .estimatedHealthOf(soldier.id)
                .shouldBe(15)
        }

        test("can get estimated health of any soldier in estimate") {
            val soldiers = listOf(
                defaultSoldier(health = 16.health),
                defaultSoldier(health = 24.health)
            )
            val estimate = AttackEstimate(soldiers[0], soldiers[1])

            estimate.estimatedHealthOf(soldiers[0].id) shouldBe 16
            estimate.estimatedHealthOf(soldiers[1].id) shouldBe 24
        }

    }



})