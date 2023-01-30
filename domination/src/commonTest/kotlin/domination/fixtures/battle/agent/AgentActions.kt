package domination.fixtures.battle.agent

import domination.battle.Agent
import domination.battle.Soldier
import domination.fixtures.battle.soldier.SoldierActions
import domination.fixtures.game
import domination.usecases.battle.Attack

class AgentActions(val agent: Agent) {

    val ends_their_turn: AgentActions
        get() = TODO("define ending a turn")

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
            game.estimateAttack(Attack.Request(agent, soldier.id, victimActions.soldier.id))
        }

    }


}