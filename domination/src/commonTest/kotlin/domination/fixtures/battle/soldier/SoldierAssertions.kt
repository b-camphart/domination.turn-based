package domination.fixtures.battle.soldier

import domination.battle.Soldier
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

interface SoldierAssertions {

    val should_be_dead: SoldierAssertions

    class Singular(val soldier: Soldier): SoldierAssertions {

        override val should_be_dead: SoldierAssertions
            get() {
                soldier.isDead.shouldBeTrue()
                return this
            }
    }

    class Plural(val soldiers: List<Soldier>) {

        fun atLeast(count: Int): SoldierAssertions = object : SoldierAssertions {
            override val should_be_dead: SoldierAssertions
                get() {
                    soldiers.count { it.isDead }.shouldBe(count)
                    return this
                }
        }

    }

}