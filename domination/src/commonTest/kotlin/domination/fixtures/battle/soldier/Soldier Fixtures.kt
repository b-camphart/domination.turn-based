@file:Suppress("PackageDirectoryMismatch", "UnusedReceiverParameter")

package domination.fixtures

import domination.battle.*
import domination.features.fighting_battles.soldiers.DefiningASoldier
import domination.fixtures.battle.soldier.SoldierActions
import domination.fixtures.battle.soldier.SoldierAssertions
import io.kotest.assertions.withClue
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

val Collection<Soldier>.ids: Set<SoldierId> get() = mapTo(HashSet(size)) { it.id }
val Collection<Soldier>.types: Set<String> get() = mapTo(HashSet(size)) { it.type }

val String.soldier get() = battle.soldiers.first { it.type == this }

val Int.health get() = SoldierHealth(this)

val Float.health get() = PercentageSoldierHealth(this)

val swordsman: Soldier get() = game.battle.soldiers.first { it.type == "swordsman" }
val Agent.swordsman: Soldier get() = battle.soldiers.first { it.type == "swordsman" && it.culture == culture }

val archer: Soldier get() = game.battle.soldiers.first { it.type == "archer" }

val archers: List<Soldier> get() = game.battle.soldiers.filter { it.type == "archer" }

val one_swordsman get() = DefiningASoldier.createSwordsman()
val one_archer get() = DefiningASoldier.createArcher()
val Int.archers get() = List(this) { DefiningASoldier.createArcher() }

fun one_of(plural: SoldierActions.Plural) = SoldierActions(plural.soldiers.first())
fun one_of(plural: SoldierAssertions.Plural) = plural.atLeast(1)

fun meleeAttack(of_strength: Number = 10) = SoldierAbility(of_strength.toInt(), "Melee")
fun meleeDefense(of_strength: Number) = SoldierAbility(of_strength.toInt(), "Melee Defense")
fun rangedDefense(of_strength: Number) = SoldierAbility(of_strength.toInt(), "Ranged Defense")
fun rangedAttack(of_strength: Number) = SoldierAbility(of_strength.toInt(), "Ranged")

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