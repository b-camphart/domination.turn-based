package domination.entities.soldier

import domination.battle.Soldier
import domination.battle.SoldierId
import domination.fixtures.meleeAttack
import domination.fixtures.meleeDefense
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs

class SoldierAttackExpenditureTest : FunSpec({

    context("creating a soldier") {

        test("without specifying if attack was expended means attack was not expended") {
            Soldier(abilities = listOf(meleeAttack(10)))
                .canAttack
                .shouldBeTrue()
        }

        test("with expended attack means attack was expended") {
            Soldier(abilities = listOf(meleeAttack(10)), attackExpended = true)
                .canAttack
                .shouldBeFalse()

            Soldier(abilities = listOf(meleeAttack(10)), attackExpended = false)
                .canAttack
                .shouldBeTrue()
        }

        test("without an attack ability means the soldier cannot attack") {
            Soldier(attackExpended = false)
                .canAttack
                .shouldBeFalse()

            Soldier(attackExpended = false, abilities = listOf(meleeDefense(10)))
                .canAttack
                .shouldBeFalse()
        }

    }

    context("expend an attack") {

        test("returns same soldier if can't attack") {
            val initialSoldier = Soldier(attackExpended = true)
            initialSoldier.withAttackExpended()
                .shouldBeSameInstanceAs(initialSoldier)
        }

        test("means the soldier cannot attack any longer") {
            Soldier(attackExpended = false, abilities = listOf(meleeAttack(10)))
                .withAttackExpended()
                .canAttack.shouldBeFalse()
        }

        test("does not modify other properties") {
            val initialSoldier = nonDefaultSoldier(attackExpended = false, abilities = listOf(meleeAttack(10)))
            initialSoldier.withAttackExpended()
                .withoutAttackExpended()
                .shouldBe(initialSoldier)
        }

    }

    context("gain attack ability") {

        test("returns same soldier if can attack") {
            val initialSoldier = Soldier(attackExpended = false, abilities = listOf(meleeAttack(10)))
            initialSoldier.withoutAttackExpended()
                .shouldBeSameInstanceAs(initialSoldier)
        }

        test("means the soldier can attack again") {
            Soldier(attackExpended = true, abilities = listOf(meleeAttack(10)))
                .withoutAttackExpended()
                .canAttack.shouldBeTrue()
        }

        test("does not modify other properties") {
            val initialSoldier = nonDefaultSoldier(attackExpended = true, abilities = listOf(meleeAttack(10)))
            initialSoldier.withoutAttackExpended()
                .withAttackExpended()
                .shouldBe(initialSoldier)
        }

    }

    val id = SoldierId()

    context("equals") {

        test("soldiers with different attack abilities are not equal") {
            Soldier(id = id, abilities = listOf(meleeAttack(10)), attackExpended = true)
                .shouldNotBe(Soldier(id = id, abilities = listOf(meleeAttack(10)), attackExpended = false))
        }

        test("soldiers with same attack abilities may be equal") {
            Soldier(id = id, abilities = listOf(meleeAttack(10)), attackExpended = true)
                .shouldBe(Soldier(id = id, abilities = listOf(meleeAttack(10)), attackExpended = true))

            Soldier(id = id, abilities = listOf(meleeAttack(10)), attackExpended = false)
                .shouldBe(Soldier(id = id, abilities = listOf(meleeAttack(10)), attackExpended = false))
        }

    }

    context("hash code") {

        test("soldiers with different attack abilities have different hash codes") {
            Soldier(id = id, abilities = listOf(meleeAttack(10)), attackExpended = true)
                .shouldNotHaveSameHashCodeAs(
                    Soldier(id = id, abilities = listOf(meleeAttack(10)), attackExpended = false)
                )
        }

        test("soldiers with same attack abilities may have same hash codes") {
            Soldier(id = id, abilities = listOf(meleeAttack(10)), attackExpended = true)
                .shouldHaveSameHashCodeAs(Soldier(id = id, abilities = listOf(meleeAttack(10)), attackExpended = true))

            Soldier(id = id, abilities = listOf(meleeAttack(10)), attackExpended = false)
                .shouldHaveSameHashCodeAs(Soldier(id = id, abilities = listOf(meleeAttack(10)), attackExpended = false))
        }

    }

})