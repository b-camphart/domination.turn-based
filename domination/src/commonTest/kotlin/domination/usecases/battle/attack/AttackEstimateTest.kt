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
            AttackEstimate()
                .estimatedHealthOf(SoldierId())
                .shouldBeNull()

            AttackEstimate(defaultSoldier())
                .estimatedHealthOf(SoldierId())
                .shouldBeNull()
        }

        test("can get estimated health of soldier in estimate") {
            val soldier = defaultSoldier(health = 15.health)
            AttackEstimate(soldier)
                .estimatedHealthOf(soldier.id)
                .shouldBe(15)
        }

    }



})