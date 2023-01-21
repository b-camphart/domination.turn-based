package domination.features.fighting_battles.ending_the_battle

import bdd.*
import domination.Culture
import domination.fixtures.battle.soldier.SoldiersArrangements
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
            .And { there_is(1.swordsman).in_the_battle }
            .And { it is_allied_with the(player) }
            .And { there_is(1.archer).in_the_battle }
            .And { it is_allied_with the(enemy) }

        When { the(player).instructs(the(swordsman)).to_attack(the(archer)) }

        Then { the(archer).should_be_dead }
            .And { the(battle).should_be_over }
            .And { the(player) should_be_the_winner_of the(battle) }
    }

}) {

    companion object {

        val player: Agent = Agent(Culture())
        val enemy: Agent = Agent(Culture())

        val battle: Battle get() = game.battle

        val swordsman: Soldier get() = game.battle.soldiers.first { it.type == "swordsman" }

        val archer: Soldier get() = game.battle.soldiers.first { it.type == "archer" }

        val Int.swordsman get() = List(this) { Soldier("swordsman", false) }
        val Int.archer get() = List(this) { Soldier("archer", false) }

        val GivenScope.a_battle_has_been_started: Unit get() = Unit

        fun GivenScope.there_is(soldiers: List<Soldier>) = SoldiersArrangements(soldiers)

        fun GivenScope.the(agent: Agent) = Any()

        fun WhenScope.the(agent: Agent) = AgentActions(agent)

        fun WhenScope.the(soldier: Soldier) = SoldierActions(soldier)

        fun ThenScope.the(agent: Agent) = AgentAssertions(agent)

        fun ThenScope.the(soldier: Soldier) = SoldierAssertions(soldier)

        fun ThenScope.the(battle: Battle) = BattleAssertions(battle)

    }

}