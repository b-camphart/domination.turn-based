package domination.fixtures.battle.agent

import domination.battle.Agent
import domination.battle.Soldier
import domination.fixtures.battle.soldier.SoldierActions
import domination.fixtures.game

class AgentActions(val agent: Agent) {

    infix fun instructs(soldierActions: SoldierActions) = Instructions(soldierActions.soldier)

    infix fun uses(soldierActions: SoldierActions) = Uses(soldierActions.soldier)

    inner class Instructions(val soldier: Soldier) {

        suspend infix fun to_attack(victimActions: SoldierActions) {
            val victim = victimActions.soldier

            game.attack(agent = agent, attacker = soldier, victim = victim)

        }

    }

    inner class Uses(val soldier: Soldier) {

        suspend infix fun to_estimate_an_attack_against(victimActions: SoldierActions) {
            game.estimateAttack(victimActions.soldier.id)
        }

    }


}