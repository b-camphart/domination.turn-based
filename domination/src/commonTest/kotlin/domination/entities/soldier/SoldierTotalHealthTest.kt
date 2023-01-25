package domination.entities.soldier

import domination.battle.Soldier
import domination.battle.SoldierHealth
import domination.battle.SoldierId
import domination.entities.soldier.SoldierHealthTest.Companion.NON_DEFAULT_SOLDIER_HEALTH
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs

class SoldierTotalHealthTest : FunSpec({

    context("creating a soldier") {

        test("without total health uses default") {
            Soldier()
                .totalHealth shouldBe DEFAULT_SOLDIER_TOTAL_HEALTH
        }

        test("without total health, but with health means the soldier has a total health equaling the initial health") {
            Soldier(health = NON_DEFAULT_SOLDIER_HEALTH)
                .totalHealth shouldBe NON_DEFAULT_SOLDIER_HEALTH
        }

        test("with total health means the soldier has the provided total health") {
            Soldier(totalHealth = NON_DEFAULT_SOLDIER_TOTAL_HEALTH)
                .totalHealth shouldBe NON_DEFAULT_SOLDIER_TOTAL_HEALTH
        }

    }

    context("changing total health") {

        test("updates the total health of the soldier") {
            Soldier(totalHealth = SoldierHealth(12))
                .withTotalHealth(newTotal = 16)
                .totalHealth shouldBe SoldierHealth(16)
        }

        test("with the same value returns the same soldier") {
            val initialSoldier = Soldier(totalHealth = SoldierHealth(12))
            initialSoldier.withTotalHealth(12)
                .shouldBeSameInstanceAs(initialSoldier)
        }

        test("does not modify other soldier properties") {
            val initialSoldier = nonDefaultSoldier(totalHealth = SoldierHealth(12))
            initialSoldier.withTotalHealth(16)
                .withTotalHealth(12)
                .shouldBe(initialSoldier)
        }

    }

    val id = SoldierId()

    context("equals") {

        test("soldiers with different totalHealths are not equal") {
            Soldier(id = id, totalHealth = SoldierHealth(10)) shouldNotBe Soldier(id = id, totalHealth = SoldierHealth(20))
        }

        test("soldiers with the same totalHealths can be equal") {
            Soldier(id = id, totalHealth = SoldierHealth(15)) shouldBe Soldier(id = id, totalHealth = SoldierHealth(15))
        }

    }

    context("hashcode") {

        test("soldiers with different totalHealths have different hash codes") {
            Soldier(id = id, totalHealth = SoldierHealth(10))
                .shouldNotHaveSameHashCodeAs(Soldier(id = id, totalHealth = SoldierHealth(20)))
        }

        test("soldiers with the same totalHealths can have the same hash code") {
            Soldier(id = id, totalHealth = SoldierHealth(15))
                .shouldHaveSameHashCodeAs(Soldier(id = id, totalHealth = SoldierHealth(15)))
        }

    }

}) {

    companion object {
        val DEFAULT_SOLDIER_TOTAL_HEALTH = SoldierHealth(10)
        val NON_DEFAULT_SOLDIER_TOTAL_HEALTH = SoldierHealth(15)
        init {
            assert(DEFAULT_SOLDIER_TOTAL_HEALTH != NON_DEFAULT_SOLDIER_TOTAL_HEALTH)
        }
    }

}