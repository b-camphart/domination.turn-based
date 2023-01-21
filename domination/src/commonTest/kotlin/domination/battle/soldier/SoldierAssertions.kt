package domination.battle.soldier

import domination.battle.Soldier
import io.kotest.matchers.booleans.shouldBeTrue

class SoldierAssertions(val soldier: Soldier) {

    val should_be_dead: SoldierAssertions get() {
        soldier.isDead.shouldBeTrue()
        return this
    }

}