@file:Suppress("PackageDirectoryMismatch")

package domination.fixtures

import domination.battle.Soldier
import domination.battle.SoldierHealth

val Collection<Soldier>.types: Set<String> get() = mapTo(HashSet(size)) { it.type }

val String.soldier get() = battle.soldiers.first { it.type == this }

val Int.health get() = SoldierHealth(this)