package domination.entities.soldier

import domination.battle.Soldier
import domination.battle.SoldierId
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldBeBlank
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs

class SoldierTypeTest : FunSpec({

    context("creating a soldier") {

        test("with a type means the soldier is that type") {
            Soldier(type = "Banana")
                .type shouldBe "Banana"
        }

        test("without a type means the soldier has no type") {
            Soldier()
                .type
                .shouldBeBlank()
        }

    }

    val id = SoldierId()

    context("equals") {

        test("soldiers with different types are not equal") {
            Soldier(id = id, type = "Type1") shouldNotBe Soldier(id = id, type = "Type2")
        }

        test("soldiers with the same type can be equal") {
            Soldier(id = id, type = "Type1") shouldBe Soldier(id = id, type = "Type1")
        }

    }

    context("hash code") {

        test("soldiers with different types have different hash codes") {
            Soldier(id = id, type = "Type1") shouldNotHaveSameHashCodeAs Soldier(id = id, type = "Type2")
        }

        test("soldiers with the same type can have the same hash code") {
            Soldier(id = id, type = "Type1") shouldHaveSameHashCodeAs Soldier(id = id, type = "Type1")
        }

    }

}) {

    companion object {
        val NON_DEFAULT_SOLDIER_TYPE = "Test Soldier"
        val DEFAULT_SOLDIER_TYPE = ""
    }

}