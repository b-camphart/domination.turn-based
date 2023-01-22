package domination.fixtures.battle.soldier

import domination.battle.Soldier
import domination.fixtures.battle.BattleActions
import domination.fixtures.game

class SoldierActions(val soldier: Soldier) {

    infix fun is_added_to(battleActions: BattleActions): SoldierActions {
        game.addSoldiers(listOf(soldier))
        return this
    }

    class Plural(val soldiers: List<Soldier>) {

    }

}