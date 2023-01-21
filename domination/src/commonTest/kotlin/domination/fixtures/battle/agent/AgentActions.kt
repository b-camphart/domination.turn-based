package domination.fixtures.battle.agent

import domination.battle.Agent
import domination.battle.Soldier
import domination.fixtures.battle.soldier.SoldierActions
import domination.fixtures.game

class AgentActions(val agent: Agent) {

    infix fun instructs(soldierActions: SoldierActions) = Instructions(soldierActions.soldier)

    inner class Instructions(val soldier: Soldier) {

        suspend infix fun to_attack(victimActions: SoldierActions) {
            val victim = victimActions.soldier

            game.attack(agent = agent, attacker = soldier, victim = victim).join()

        }

    }

}