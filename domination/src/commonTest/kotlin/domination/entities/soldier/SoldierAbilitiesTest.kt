package domination.entities.soldier

import domination.battle.Soldier
import domination.battle.SoldierAbility
import domination.battle.SoldierId
import domination.fixtures.meleeAttack
import domination.fixtures.meleeDefense
import domination.fixtures.rangedAttack
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs

class SoldierAbilitiesTest : FunSpec({

    context("creating a soldier") {
        val ability = SoldierAbility(10, "")

        test("with no abilities means it has no ability") {
            Soldier(abilities = emptyList())
                .hasAbility(ability)
                .shouldBeFalse()
        }

        test("with an abilities means it has the ability") {
            Soldier(abilities = listOf(ability))
                .hasAbility(ability)
                .shouldBeTrue()
        }

    }

    context("equals") {
        val id = SoldierId()

        val firstAbility = SoldierAbility(10, "first")
        val secondAbility = SoldierAbility(10, "second")

        test("two soldiers with differing abilities are not equal") {
            Soldier(id = id, abilities = emptyList()) shouldNotBe Soldier(id = id, abilities = listOf(firstAbility))

            Soldier(id = id, abilities = listOf(secondAbility))
                .shouldNotBe(Soldier(id = id, abilities = listOf(firstAbility)))
        }

        test("soldiers with same abilities can be equal") {
            Soldier(id = id, abilities = listOf()) shouldBe Soldier(id = id, abilities = listOf())

            Soldier(id = id, abilities = listOf(firstAbility))
                .shouldBe(Soldier(id = id, abilities = listOf(firstAbility)))

            Soldier(id = id, abilities = listOf(secondAbility))
                .shouldBe(Soldier(id = id, abilities = listOf(secondAbility)))

            Soldier(id = id, abilities = listOf(firstAbility, secondAbility))
                .shouldBe(Soldier(id = id, abilities = listOf(firstAbility, secondAbility)))
        }

    }

    context("adding an ability") {
        val attack = meleeAttack(10)

        test("adds the ability to the soldier") {
            Soldier(abilities = emptyList())
                .withAttack(attack)
                .hasAbility(attack)
                .shouldBeTrue()
        }

        test("does not modify other properties") {
            val initialSoldier = nonDefaultSoldier(abilities = emptyList())
            val modifiedSoldier = initialSoldier
                .withAttack(attack)
                .withoutAttack(attack)

            modifiedSoldier shouldBe initialSoldier
        }

        test("does not remove other abilities") {
            val otherAttack = rangedAttack(20)
            Soldier(abilities = listOf(otherAttack))
                .withAttack(attack)
                .hasAbility(otherAttack)
                .shouldBeTrue()
        }

    }

    context("removing an ability") {
        val attack = meleeAttack(10)

        test("removes the ability from the soldier") {
            Soldier(abilities = listOf(attack))
                .withoutAttack(attack)
                .hasAbility(attack)
                .shouldBeFalse()
        }

        test("does not modify other properties") {
            val initialSoldier = nonDefaultSoldier(abilities = listOf(attack))
            val modifiedSoldier = initialSoldier
                .withoutAttack(attack)
                .withAttack(attack)

            modifiedSoldier shouldBe initialSoldier
        }

        test("only removes the specified ability") {
            val otherAttack = rangedAttack(20)
            Soldier(abilities = listOf(attack, otherAttack))
                .withoutAttack(attack)
                .hasAbility(otherAttack)
                .shouldBeTrue()
        }

    }

    context("hash code") {
        val id = SoldierId()

        test("soldiers with different abilities have different hash codes") {
            Soldier(id = id, abilities = emptyList()) shouldNotHaveSameHashCodeAs Soldier(id = id, abilities = listOf(meleeAttack(10)))
            Soldier(id = id, abilities =listOf(meleeAttack(10)))
                .shouldNotHaveSameHashCodeAs(Soldier(id = id, abilities = listOf(meleeAttack(10), meleeDefense(10))))
            Soldier(id = id, abilities =listOf(meleeAttack(20)))
                .shouldNotHaveSameHashCodeAs(Soldier(id = id, abilities = listOf(meleeAttack(10))))
        }

        test("soldiers with the same abilities can have the same hash code") {
            Soldier(id = id, abilities = emptyList()) shouldHaveSameHashCodeAs Soldier(id = id, abilities = listOf())
            Soldier(id = id, abilities =listOf(meleeAttack(10)))
                .shouldHaveSameHashCodeAs(Soldier(id = id, abilities = listOf(meleeAttack(10))))
        }

    }

})