package domination.fixtures

import bdd.ThenScope
import domination.fixtures.battle.ConfigurableDominationBattle
import domination.fixtures.battle.AttackEstimateAssertions
import domination.usecases.battle.AttackEstimate

var game: ConfigurableDominationBattle = ConfigurableDominationBattle(player.culture)

val estimate: AttackEstimate get() = game.attackEstimate!!

fun ThenScope.the(estimate: AttackEstimate) = AttackEstimateAssertions(estimate)