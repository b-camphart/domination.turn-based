package domination.entities.soldier

import domination.battle.Soldier
import domination.battle.SoldierId
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class SoldierIdTest : FunSpec({

    context("creating a soldier") {
        val id = SoldierId()

        test("with an id means the soldier has that id") {
            Soldier(id = id).id shouldBe id
        }

        test("without an id creates a unique id for that soldier") {
            Soldier().id.should {
                it.shouldNotBeNull()
                it shouldNotBe id
            }
        }

    }

    context("equals") {

        test("soldiers with different ids are not equal") {
            Soldier() shouldNotBe Soldier()
            Soldier(id = SoldierId()) shouldNotBe Soldier(id = SoldierId())
        }

        test("soldiers with same ids can be equal") {
            val id = SoldierId()
            Soldier(id = id) shouldBe Soldier(id = id)
        }

    }

})