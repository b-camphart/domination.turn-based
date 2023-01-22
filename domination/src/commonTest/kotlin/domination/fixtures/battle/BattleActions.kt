package domination.fixtures.battle

import domination.battle.Battle
import domination.fixtures.game

class BattleActions(val battle: Battle) {

    val is_started: BattleActions get() {
        game.start_battle()
        return this
    }

}