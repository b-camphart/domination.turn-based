package domination.fixtures.battle.soldier

import domination.battle.Agent
import domination.battle.Soldier
import domination.battle.SoldierHealth
import domination.fixtures.PercentageSoldierHealth
import domination.fixtures.game
import domination.fixtures.health

class SoldierArrangements(val soldier: Soldier) {

    val in_the_battle: SoldierArrangements
        get() {
        game.addSoldiers(listOf(soldier))
        return this@SoldierArrangements
    }

    infix fun is_allied_with(ally: Agent): SoldierArrangements {
        val newSoldier = Soldier(soldier.id, soldier.type, soldier.health, soldier.totalHealth, ally.culture, soldier.abilities)
        if (game.battle.soldiers.contains(soldier)) {
            game.replaceSoldier(soldier, newSoldier)
        }
        return SoldierArrangements(newSoldier)
    }

    infix fun is_at(health: SoldierHealth): SoldierArrangements {
        val newSoldier = soldier.withHealth(health.value)
        if (game.battle.soldiers.contains(soldier)) {
            game.replaceSoldier(soldier, newSoldier)
        }
        return SoldierArrangements(newSoldier)
    }

    infix fun is_at(percentageHealth: PercentageSoldierHealth): SoldierArrangements {
        return is_at((soldier.totalHealth.value * percentageHealth.percentage).toInt().health)
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

        infix fun are_at(health: SoldierHealth): Plural {
            return Plural(
                soldiers.map { SoldierArrangements(it).is_at(health).soldier }
            )
        }

    }

}