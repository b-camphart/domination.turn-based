package domination.features.fighting_battles.ending_the_battle

import bdd.*
import domination.Culture
import domination.fixtures.battle.soldier.SoldierArrangements
import domination.battle.*
import domination.fixtures.battle.agent.AgentActions
import domination.fixtures.battle.agent.AgentAssertions
import domination.fixtures.battle.soldier.SoldierActions
import domination.fixtures.battle.soldier.SoldierAssertions
import domination.fixtures.battle.BattleAssertions
import domination.fixtures.game
import io.kotest.core.spec.style.StringSpec

/**
 * To win the battle, the player must destroy all enemy units in the battle, which then ends the battle.
 */
class WinningTheBattle : StringSpec({

    "Scenario: Destroy last enemy" {
        Given { a_battle_has_been_started }
            .And { there_is(one_swordsman).in_the_battle }
            .And { it is_allied_with the(player) }
            .And { there_is(one_archer).in_the_battle }
            .And { it is_allied_with the(enemy) }

        When { the(player).instructs(the(swordsman)).to_attack(the(archer)) }

        Then { the(archer).should_be_dead }
            .And { the(battle).should_be_over }
            .And { the(player) should_be_the_winner_of the(battle) }
    }

    "Scenario: Destroy an enemy, but one remains" {
        Given { a_battle_has_been_started }
            .And { there_is(one_swordsman).in_the_battle }
            .And { it is_allied_with the(player) }
            .And { there_are(2.archers).in_the_battle }
            .And { the(archers) are_allied_with the(enemy) }

        When { the(player).instructs(the(swordsman)).to_attack(one_of(the(archers))) }

        Then { one_of(the(archers)).should_be_dead }
            .But { the(battle).should_not_be_over }
            .And { it.should_not_have_a_winner }
    }

}) {

    companion object {

        val player: Agent = Agent(Culture())
        val enemy: Agent = Agent(Culture())

        val battle: Battle get() = game.battle

        val swordsman: Soldier get() = game.battle.soldiers.first { it.type == "swordsman" }

        val archer: Soldier get() = game.battle.soldiers.first { it.type == "archer" }

        val archers: List<Soldier> get() = game.battle.soldiers.filter { it.type == "archer" }

        val one_swordsman get() = Soldier("swordsman", false)
        val one_archer get() = Soldier("archer", false)
        val Int.archers get() = List(this) { Soldier("archer", false) }

        fun one_of(plural: SoldierActions.Plural) = SoldierActions(plural.soldiers.first())
        fun one_of(plural: SoldierAssertions.Plural) = plural.atLeast(1)

        val GivenScope.a_battle_has_been_started: Unit get() = Unit

        fun GivenScope.there_is(soldier: Soldier) = SoldierArrangements(soldier)

        fun GivenScope.there_are(soldiers: List<Soldier>) = SoldierArrangements.Plural(soldiers)

        fun GivenScope.the(agent: Agent) = Any()

        fun GivenScope.the(soldiers: List<Soldier>) = SoldierArrangements.Plural(soldiers)

        fun WhenScope.the(agent: Agent) = AgentActions(agent)

        fun WhenScope.the(soldier: Soldier) = SoldierActions(soldier)

        fun WhenScope.the(soldiers: List<Soldier>) = SoldierActions.Plural(soldiers)

        fun ThenScope.the(agent: Agent) = AgentAssertions(agent)

        fun ThenScope.the(soldier: Soldier): SoldierAssertions = SoldierAssertions.Singular(soldier)

        fun ThenScope.the(soldiers: List<Soldier>) = SoldierAssertions.Plural(soldiers)

        fun ThenScope.the(battle: Battle) = BattleAssertions(battle)

    }

}