package domination.battle.end

import bdd.*
import domination.SomethingArrangements
import domination.battle.Agent
import domination.battle.Battle
import domination.battle.BattleAssertions
import domination.battle.Soldier
import domination.battle.agent.AgentActions
import domination.battle.agent.AgentAssertions
import domination.battle.soldier.SoldierActions
import domination.battle.soldier.SoldierAssertions
import io.kotest.core.spec.style.StringSpec

/**
 * To win the battle, the player must destroy all enemy units in the battle, which then ends the battle.
 */
class WinTheBattleFeature : StringSpec({

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

        val player: Agent = Agent()
        val enemy: Agent = Agent()

        val battle: Battle = Battle()

        val swordsman get() = Soldier()

        val archer get() = Soldier()

        val Int.swordsman get() = Any()
        val Int.archer get() = Any()

        val GivenScope.a_battle_has_been_started: Unit get() = Unit

        fun GivenScope.there_is(something: Any) = SomethingArrangements(something)

        fun GivenScope.the(agent: Agent) = Any()

        fun WhenScope.the(agent: Agent) = AgentActions(agent)

        fun WhenScope.the(soldier: Soldier) = SoldierActions(soldier)

        fun ThenScope.the(agent: Agent) = AgentAssertions(agent)

        fun ThenScope.the(soldier: Soldier) = SoldierAssertions(soldier)

        fun ThenScope.the(battle: Battle) = BattleAssertions(battle)

    }

}