package domination.entities.battle

import domination.battle.Battle
import domination.battle.Soldier
import domination.battle.SoldierHealth
import domination.battle.SoldierId
import domination.entities.soldier.defaultSoldier
import domination.fixtures.health
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class BattleSoldiersTest : FunSpec({

    val soldier1 = Soldier()

    context("creating battle") {

        test("without soldiers means there are no soldiers in the battle") {
            val battle = Battle()

            battle.soldiers.shouldBeEmpty()
            battle.getSoldier(soldier1.id).shouldBeNull()
        }

        test("with soldiers means those soldiers are in the battle") {
            val battle = Battle(soldiers = listOf(soldier1))
            battle.getSoldier(soldier1.id) shouldBe soldier1
        }

    }

    context("getting soldiers") {

        test("only soldiers in the battle are retrievable") {
            val battle = Battle(soldiers = List(5) { defaultSoldier() })
            battle.getSoldier(SoldierId()).shouldBeNull()
        }

        test("correct soldier is retrieved") {
            val soldiers = List(5) { defaultSoldier() }
            val battle = Battle(soldiers = soldiers)
            battle.getSoldier(soldiers[2].id) shouldBe soldiers[2]
        }

    }

    context("adding a soldier") {

        test("should add the soldier to the battle") {

            Battle()
                .withSoldier(soldier1)
                .getSoldier(soldier1.id) shouldBe soldier1

        }

        test("should not remove other soldiers from the battle") {
            val soldier2 = Soldier()

            Battle(soldiers = listOf(soldier2))
                .withSoldier(soldier1)
                .getSoldier(soldier2.id) shouldBe soldier2
        }

        test("should replace soldier with same id") {
            val soldier2 = Soldier(health = SoldierHealth(10))
            Battle(soldiers = listOf(soldier2))
                .withSoldier(soldier2.withHealth(20))
                .getSoldier(soldier2.id).shouldNotBeNull()
                .health shouldBe 20.health
        }

    }

})