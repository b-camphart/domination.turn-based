package domination.fixtures.battle.soldier

import domination.battle.Soldier
import domination.fixtures.game

class SoldierArrangements(val soldier: Soldier) {

    val in_the_battle: SoldierArrangements
        get() {
        game.addSoldiers(listOf(soldier))
        return this@SoldierArrangements
    }

    infix fun is_allied_with(ally: Any): SoldierArrangements {

        return this
    }

    class Plural(val soldiers: List<Soldier>) {

        val in_the_battle: Plural
            get() {
                game.addSoldiers(soldiers)
                return this
            }

        infix fun are_allied_with(ally: Any): Plural {

            return this
        }

    }

}