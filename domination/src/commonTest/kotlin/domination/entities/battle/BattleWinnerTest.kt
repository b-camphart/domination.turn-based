package domination.entities.battle

import domination.Culture
import domination.battle.Battle
import domination.entities.soldier.defaultSoldier
import domination.fixtures.health
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class BattleWinnerTest : FunSpec({

    context("creating a battle") {

        test("without any soldiers means the battle has no winner") {
            Battle().winner.shouldBeNull()
        }

        test("with only soldiers belonging to one culture means that culture has won") {
            val singleCulture = Culture()
            Battle(soldiers = List(5) { defaultSoldier(culture = singleCulture) })
                .winner.shouldBe(singleCulture)
        }

        test("with soldiers belonging to multiple cultures means no culture has won yet") {
            Battle(soldiers = List(5) { defaultSoldier(culture = Culture()) })
                .winner.shouldBeNull()
        }

        test("with living soldiers only belonging to one culture means that culture has won") {
            val winningCulture = Culture()
            Battle(
                soldiers = List(2) { defaultSoldier(culture = Culture(), health = 0.health) } +
                        defaultSoldier(culture = winningCulture) +
                        List(2) { defaultSoldier(culture = Culture(), health = 0.health) }
            )
                .winner.shouldBe(winningCulture)
        }
    }

})