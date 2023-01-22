package domination.fixtures

import bdd.GivenScope
import bdd.ThenScope
import bdd.WhenScope
import domination.Culture
import domination.battle.*
import domination.features.fighting_battles.soldiers.DefiningASoldier.Companion.createArcher
import domination.features.fighting_battles.soldiers.DefiningASoldier.Companion.createSwordsman
import domination.fixtures.battle.ConfigurableDominationBattle
import domination.fixtures.battle.BattleActions
import domination.fixtures.battle.BattleArrangements
import domination.fixtures.battle.BattleAssertions
import domination.fixtures.battle.agent.AgentActions
import domination.fixtures.battle.agent.AgentAssertions
import domination.fixtures.battle.soldier.*

var game: ConfigurableDominationBattle = ConfigurableDominationBattle()



val player: Agent = Agent(Culture())
val enemy: Agent = Agent(Culture())

val battle: Battle get() = game.battle

val swordsman: Soldier get() = game.battle.soldiers.first { it.type == "swordsman" }
val Agent.swordsman: Soldier get() = battle.soldiers.first { it.type == "swordsman" && it.culture == culture }

val archer: Soldier get() = game.battle.soldiers.first { it.type == "archer" }

val archers: List<Soldier> get() = game.battle.soldiers.filter { it.type == "archer" }

val one_swordsman get() = createSwordsman()
val one_archer get() = createArcher()
val Int.archers get() = List(this) { createArcher() }

fun one_of(plural: SoldierActions.Plural) = SoldierActions(plural.soldiers.first())
fun one_of(plural: SoldierAssertions.Plural) = plural.atLeast(1)

fun meleeAttack(of_strength: Number) = SoldierAbility(of_strength.toInt(), "Melee")
fun meleeDefense(of_strength: Number) = SoldierAbility(of_strength.toInt(), "Melee Defense")
fun rangedDefense(of_strength: Number) = SoldierAbility(of_strength.toInt(), "Ranged Defense")
fun rangedAttack(of_strength: Number) = SoldierAbility(of_strength.toInt(), "Ranged")

val GivenScope.a_battle_has_been_started: Unit get() = Unit

fun GivenScope.a_soldier_with_a(ability: SoldierAbility): SoldierTypeArrangements = SoldierTypeArrangements(ability)

fun GivenScope.a_soldier_called(typeName: String) = SoldierTypeArrangements(typeName)
fun GivenScope.there_is(soldier: Soldier) = SoldierArrangements(soldier)

fun GivenScope.there_are(soldiers: List<Soldier>) = SoldierArrangements.Plural(soldiers)

fun GivenScope.the(agent: Agent) = agent

fun GivenScope.the(battle: Battle) = BattleArrangements(battle)

fun GivenScope.the(soldiers: List<Soldier>) = SoldierArrangements.Plural(soldiers)

fun WhenScope.the(battle: Battle) = BattleActions(battle)

fun WhenScope.a_soldier_called_a(typeName: String) =
    SoldierActions(game.soldierTypes.getValue(typeName).invoke())

fun WhenScope.the(agent: Agent) = AgentActions(agent)

fun WhenScope.the(soldier: Soldier) = SoldierActions(soldier)

fun WhenScope.the(soldiers: List<Soldier>) = SoldierActions.Plural(soldiers)

fun ThenScope.the(agent: Agent) = AgentAssertions(agent)

fun ThenScope.the(soldier: Soldier): SoldierAssertions = SoldierAssertions.Singular(soldier)

fun ThenScope.the(soldiers: List<Soldier>) = SoldierAssertions.Plural(soldiers)

fun ThenScope.the(battle: Battle) = BattleAssertions(battle)

fun ThenScope.there_should_be_soldier_called_a(typeName: String) = SoldierTypeAssertions(typeName)