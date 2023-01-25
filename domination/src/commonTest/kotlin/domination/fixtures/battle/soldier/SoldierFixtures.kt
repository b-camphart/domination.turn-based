@file:Suppress("PackageDirectoryMismatch")

package domination.fixtures

import domination.battle.Soldier
import domination.battle.SoldierHealth
import io.kotest.assertions.withClue
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

val Collection<Soldier>.types: Set<String> get() = mapTo(HashSet(size)) { it.type }

val String.soldier get() = battle.soldiers.first { it.type == this }

val Int.health get() = SoldierHealth(this)

val Float.health get() = PercentageSoldierHealth(this)

fun Soldier.shouldBeDead(): Soldier {
    withClue("soldier should be dead, but health was ${health.value}") {
        isDead.shouldBeTrue()
    }
    return this
}

fun Soldier.shouldNotBeDead(): Soldier {
    withClue("soldier not should be dead, but health was ${health.value}") {
        isDead.shouldBeFalse()
    }
    return this
}

class PercentageSoldierHealth(val percentage: Float)