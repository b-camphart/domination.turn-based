package domination.entities.soldier

import domination.battle.Soldier
import domination.battle.SoldierHealth
import domination.battle.SoldierId
import domination.fixtures.health
import domination.fixtures.shouldBeDead
import domination.fixtures.shouldNotBeDead
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class SoldierHealthTest : FunSpec({

    context("creating a soldier") {

        test("with health sets the health of the soldier") {
            Soldier(health = NON_DEFAULT_SOLDIER_HEALTH)
                .health shouldBe NON_DEFAULT_SOLDIER_HEALTH
        }

        test("without health defaults to health of 10") {
            Soldier()
                .health shouldBe DEFAULT_SOLDIER_HEALTH
        }

        test("without health, but with total health defaults to health the total health") {
            Soldier(totalHealth = NON_DEFAULT_SOLDIER_HEALTH)
                .health shouldBe NON_DEFAULT_SOLDIER_HEALTH
        }

        test("with no health creates a dead soldier") {
            Soldier(health = 0.health)
                .shouldBeDead()
        }

        test("with health above zero creates a living soldier") {
            Soldier(health = 1.health)
                .shouldNotBeDead()
        }

    }

    context("updating health") {

        test("updates the soldier's health") {
            val newHealth = 10
            Soldier(health = SoldierHealth(20))
                .withHealth(newHealth)
                .health shouldBe SoldierHealth(10)
        }

        test("does not modify other properties") {
            val initialSoldier = nonDefaultSoldier(health = SoldierHealth(20))
            val modifiedSoldier = initialSoldier
                .withHealth(10)
                .withHealth(20)
            modifiedSoldier shouldBe initialSoldier
        }

        test("kills the soldier if zero or less") {
            Soldier().withHealth(0).shouldBeDead()
            Soldier().withHealth(-1).shouldBeDead()
        }

        test("does not kill the soldier if greater than zero") {
            Soldier().withHealth(1).shouldNotBeDead()
        }

    }

    context("equals") {

        val id = SoldierId()

        test("soldiers with different healths are not equal") {
            Soldier(id = id, health = SoldierHealth(20)) shouldNotBe Soldier(id = id, health = SoldierHealth(10))
        }

        test("soldiers with same health can be equal") {
            Soldier(id = id, health = SoldierHealth(20)) shouldBe Soldier(id = id, health = SoldierHealth(20))
        }

    }

}) {

    companion object {
        val DEFAULT_SOLDIER_HEALTH = SoldierHealth(10)
        val NON_DEFAULT_SOLDIER_HEALTH = SoldierHealth(15)
        init {
            assert(NON_DEFAULT_SOLDIER_HEALTH != DEFAULT_SOLDIER_HEALTH)
        }
    }

}