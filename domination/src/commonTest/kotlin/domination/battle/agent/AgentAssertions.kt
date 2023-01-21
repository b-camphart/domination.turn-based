package domination.battle.agent

import domination.battle.Agent
import domination.battle.BattleAssertions

class AgentAssertions(val agent: Agent) {

    infix fun should_be_the_winner_of(battleAssertions: BattleAssertions): AgentAssertions {
        return this
    }

}