package domination.battle.agent

import domination.battle.Agent
import domination.battle.BattleAssertions
import io.kotest.matchers.shouldBe

class AgentAssertions(val agent: Agent) {

    infix fun should_be_the_winner_of(battleAssertions: BattleAssertions): AgentAssertions {
        battleAssertions.battle.winner shouldBe agent.culture
        return this
    }

}