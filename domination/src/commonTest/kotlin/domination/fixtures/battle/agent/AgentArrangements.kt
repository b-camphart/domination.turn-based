package domination.fixtures.battle.agent

import domination.battle.Agent
import domination.battle.Soldier
import domination.fixtures.battle.soldier.SoldierArrangements
import domination.fixtures.game

class AgentArrangements(val agent: Agent) {
    fun has_instructed(soldierArrangements: SoldierArrangements): Instructions =
        Instructions(soldierArrangements.soldier)

    inner class Instructions(val soldier: Soldier) {
        suspend fun to_attack(victimArrangements: SoldierArrangements) {
            val victim = victimArrangements.soldier

            game.attack(agent = agent, attacker = soldier, victim = victim)
        }


    }

}