package domination.fixtures.battle

import domination.battle.Battle
import io.kotest.matchers.booleans.shouldBeTrue

class BattleAssertions(val battle: Battle) {

    val should_be_over: BattleAssertions
        get() {
        battle.isOver.shouldBeTrue()
        return this
    }

}