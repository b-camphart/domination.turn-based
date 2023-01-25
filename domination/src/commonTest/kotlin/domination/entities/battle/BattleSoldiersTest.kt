package domination.entities.battle

import domination.battle.Battle
import domination.battle.Soldier
import domination.battle.SoldierHealth
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

class BattleSoldiersTest : FunSpec({

    val soldier1 = Soldier()

    context("creating battle") {

        test("with soldiers means those soldiers are in the battle") {
            Battle(soldiers = listOf(soldier1)).soldiers shouldBe listOf(soldier1)
        }

    }

    context("adding a soldier") {

        test("should add the soldier to the battle") {

            Battle()
                .withSoldier(soldier1)
                .soldiers shouldContain soldier1

        }

        test("should not remove other soldiers from the battle") {
            val soldier2 = Soldier()

            Battle(soldiers = listOf(soldier2))
                .withSoldier(soldier1)
                .soldiers shouldContain soldier2
        }

        test("should replace soldier with same id") {
            val soldier2 = Soldier(health = SoldierHealth(10))
            Battle(soldiers = listOf(soldier2))
                .withSoldier(soldier2.withHealth(20))
                .soldiers.should {
                    it shouldContain soldier2.withHealth(20)
                    it shouldNotContain soldier2
                }
        }

    }

})