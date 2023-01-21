package domination.fixtures.battle.soldier

import domination.battle.Soldier
import domination.fixtures.game

class SoldiersArrangements(val soldiers: List<Soldier>) {

    val in_the_battle: SoldiersArrangements
        get() {
        game.addSoldiers(soldiers)
        return this@SoldiersArrangements
    }

    infix fun is_allied_with(ally: Any): SoldiersArrangements {



        return this
    }

}