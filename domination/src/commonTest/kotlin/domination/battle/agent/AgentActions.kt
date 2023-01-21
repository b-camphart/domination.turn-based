package domination.battle.agent

import domination.battle.Agent

class AgentActions(agent: Agent) {

    infix fun instructs(something: Any) = Instructions()

    inner class Instructions {

        infix fun to_attack(something: Any) {}

    }

}