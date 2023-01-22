package domination.fixtures.battle.soldier

import domination.battle.Soldier
import domination.battle.SoldierAbility
import domination.battle.SoldierHealth
import domination.fixtures.battle.BattleArrangements
import domination.fixtures.game

class SoldierTypeArrangements private constructor(
    val abilities: List<SoldierAbility>,
    val health: SoldierHealth = SoldierHealth(10),
    val typeName: String = ""
) {

    init {
        if (typeName.isNotBlank()) {
            game.soldierTypes = game.soldierTypes + (typeName to ::createSoldier)
        }
    }

    constructor(initialAbility: SoldierAbility) : this(listOf(initialAbility))
    constructor(typeName: String) : this(emptyList(), typeName = typeName)

    infix fun has_a(ability: SoldierAbility): SoldierTypeArrangements {
        return SoldierTypeArrangements(abilities + ability, health, typeName)
    }

    infix fun is_called_a(typeName: String): SoldierTypeArrangements {
        return SoldierTypeArrangements(abilities, health, typeName)
    }

    infix fun has(health: SoldierHealth): SoldierTypeArrangements {
        return SoldierTypeArrangements(abilities, health, typeName)
    }

    infix fun has_been_added_to(battle: BattleArrangements): SoldierArrangements {
        val soldier = createSoldier()
        game.addSoldiers(listOf(soldier))
        return SoldierArrangements(soldier)
    }

    private fun createSoldier() =
        Soldier(typeName, false, health, null, abilities)

}