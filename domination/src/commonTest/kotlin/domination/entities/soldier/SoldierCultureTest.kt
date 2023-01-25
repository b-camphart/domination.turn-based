package domination.entities.soldier

import domination.Culture
import domination.battle.Soldier
import domination.battle.SoldierId
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class SoldierCultureTest : FunSpec({

    context("creating a soldier") {
        val culture = Culture()

        test("without a culture means it has no culture") {
            Soldier().culture.shouldBeNull()
        }

        test("with a culture means it has that culture") {
            Soldier(culture = culture)
                .culture shouldBe culture
        }

        test("with a different culture means it does not have that culture") {
            Soldier(culture = Culture())
                .culture shouldNotBe culture
        }

    }

    context("equals") {
        val id = SoldierId()

        test("soldiers with different cultures are not equal") {
            Soldier(id = id) shouldNotBe Soldier(id = id, culture = Culture())
        }

    }

})