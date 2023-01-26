@file:Suppress("PackageDirectoryMismatch", "UnusedReceiverParameter")

/*
 * All fixtures are in the domination.fixtures package to allow features to easily import everything via
 * [import domination.fixtures.*]
 */
package domination.fixtures

import bdd.GivenScope
import bdd.ThenScope
import bdd.WhenScope
import domination.battle.Soldier
import domination.battle.SoldierAbility
import domination.fixtures.battle.soldier.*

fun GivenScope.a_soldier_with_a(ability: SoldierAbility): SoldierTypeArrangements = SoldierTypeArrangements(ability)

fun GivenScope.a_soldier_called_a(typeName: String) = SoldierTypeArrangements(typeName)
fun GivenScope.a_soldier_called_an(typeName: String) = SoldierTypeArrangements(typeName)
fun GivenScope.there_is(soldier: Soldier) = SoldierArrangements(soldier)

fun GivenScope.there_are(soldiers: List<Soldier>) = SoldierArrangements.Plural(soldiers)

fun GivenScope.the(soldiers: List<Soldier>) = SoldierArrangements.Plural(soldiers)

fun WhenScope.the(soldier: Soldier) = SoldierActions(soldier)

fun WhenScope.the(soldiers: List<Soldier>) = SoldierActions.Plural(soldiers)

fun WhenScope.a_soldier_called_a(typeName: String) =
    SoldierActions(game.soldierTypes.getValue(typeName).invoke())

fun ThenScope.the(soldier: Soldier): SoldierAssertions = SoldierAssertions.Singular(soldier)

fun ThenScope.the(soldiers: List<Soldier>) = SoldierAssertions.Plural(soldiers)

fun ThenScope.there_should_be_soldier_called_a(typeName: String) = SoldierTypeAssertions(typeName)