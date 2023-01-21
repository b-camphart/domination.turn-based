package domination.fixtures.battle

import domination.battle.Battle
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull

class BattleAssertions(val battle: Battle) {

    val should_be_over: BattleAssertions
        get() {
        battle.isOver.shouldBeTrue()
        return this
    }

    val should_not_be_over: BattleAssertions
        get() {
            battle.isOver.shouldBeFalse()
            return this
        }

    val should_not_have_a_winner: BattleAssertions
        get() {
            battle.winner.shouldBeNull()
            return this
        }

}