package domination.battle

import io.kotest.matchers.booleans.shouldBeTrue

class BattleAssertions(val battle: Battle) {

    val should_be_over: BattleAssertions get() {
        battle.isOver.shouldBeTrue()
        return this
    }

}