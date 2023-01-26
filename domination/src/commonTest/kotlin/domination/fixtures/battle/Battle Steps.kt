@file:Suppress("PackageDirectoryMismatch", "UnusedReceiverParameter")

/*
 * All fixtures are in the domination.fixtures package to allow features to easily import everything via
 * [import domination.fixtures.*]
 */
package domination.fixtures

import bdd.GivenScope
import bdd.ThenScope
import bdd.WhenScope
import domination.battle.Battle
import domination.fixtures.battle.BattleActions
import domination.fixtures.battle.BattleArrangements
import domination.fixtures.battle.BattleAssertions

val GivenScope.a_battle_has_been_started: Unit get() = Unit

fun GivenScope.the(battle: Battle) = BattleArrangements(battle)

fun WhenScope.the(battle: Battle) = BattleActions(battle)

fun ThenScope.the(battle: Battle) = BattleAssertions(battle)