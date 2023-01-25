package domination.entities.battle

import domination.Culture
import domination.battle.Battle
import domination.entities.soldier.defaultSoldier
import domination.fixtures.health
import domination.fixtures.shouldBeOver
import domination.fixtures.shouldNotBeOver
import io.kotest.core.spec.style.FunSpec

class BattleIsOverTest : FunSpec({

    context("creating a battle") {

        test("without any soldiers means the battle is over") {
            Battle().shouldBeOver()
        }

        test("with only soldiers belonging to one culture means the battle is over") {
            val singleCulture = Culture()
            Battle(soldiers = List(5) { defaultSoldier(culture = singleCulture) })
                .shouldBeOver()
        }

        test("with soldiers belonging to multiple cultures means the battle is still going") {
            Battle(soldiers = List(5) { defaultSoldier(culture = Culture()) })
                .shouldNotBeOver()
        }

        test("with living soldiers only belonging to one culture means the battle is over") {
            Battle(soldiers = List(5) {
                defaultSoldier(
                    culture = Culture(),
                    health = if (it == 2) 10.health else 0.health
                )
            })
                .shouldBeOver()
        }

        test("with all dead soldiers means the battle is over") {
            Battle(soldiers = List(5) { defaultSoldier(culture = Culture(), health = 0.health) })
                .shouldBeOver()
        }

    }

    context("adding a soldier") {
        test("adding a living soldier with a new culture to a battle that was already over makes the battle resume") {
            Battle(soldiers = listOf(defaultSoldier(culture = Culture())))
                .withSoldier(defaultSoldier(culture = Culture()))
                .shouldNotBeOver()
        }
    }

})