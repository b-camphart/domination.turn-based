package domination.fixtures.battle.soldier

import domination.battle.Soldier
import domination.battle.SoldierAbility
import domination.battle.SoldierHealth
import domination.fixtures.battle.BattleAssertions
import domination.fixtures.shouldBeDead
import domination.fixtures.shouldNotBeDead
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe

interface SoldierAssertions {

    val should_be_dead: SoldierAssertions
    val should_not_be_dead: SoldierAssertions
    infix fun should_be_in(battleAssertions: BattleAssertions): SoldierAssertions

    infix fun should_have_a(ability: SoldierAbility): SoldierAssertions

    infix fun should_have(health: SoldierHealth): SoldierAssertions

    class Singular(val soldier: Soldier) : SoldierAssertions {

        val should_not_be_able_to_attack: Singular
            get() {
                soldier.canAttack.shouldBeFalse()
                return this
            }

        val should_be_able_to_attack: Singular
            get() {
                soldier.canAttack.shouldBeTrue()
                return this
            }

        override val should_be_dead: SoldierAssertions
            get() {
                soldier.shouldBeDead()
                return this
            }

        override val should_not_be_dead: SoldierAssertions
            get() {
                soldier.shouldNotBeDead()
                return this
            }
        override fun should_be_in(battleAssertions: BattleAssertions): SoldierAssertions {
            battleAssertions.battle.soldiers.shouldContain(soldier)
            return this
        }

        override fun should_have_a(ability: SoldierAbility): SoldierAssertions {
            soldier.hasAbility(ability).shouldBeTrue()
            return this
        }

        override fun should_have(health: SoldierHealth): SoldierAssertions {
            soldier.health.shouldBe(health)
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

            override val should_not_be_dead: SoldierAssertions
                get() {
                    soldiers.count { !it.isDead }.shouldBe(count)
                    return this
                }

            override fun should_be_in(battleAssertions: BattleAssertions): SoldierAssertions {
                soldiers.count {
                    battleAssertions.battle.soldiers.contains(it)
                }.shouldBe(count)
                return this
            }

            override fun should_have_a(ability: SoldierAbility): SoldierAssertions {
                soldiers.count { it.hasAbility(ability) }.shouldBe(count)
                return this
            }

            override fun should_have(health: SoldierHealth): SoldierAssertions {
                return this
            }
        }

    }

}