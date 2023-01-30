package domination.entities.soldier

import domination.Culture
import domination.battle.Soldier
import domination.battle.SoldierAbility
import domination.battle.SoldierHealth
import domination.entities.soldier.SoldierHealthTest.Companion.DEFAULT_SOLDIER_HEALTH
import domination.entities.soldier.SoldierHealthTest.Companion.NON_DEFAULT_SOLDIER_HEALTH
import domination.entities.soldier.SoldierTotalHealthTest.Companion.DEFAULT_SOLDIER_TOTAL_HEALTH
import domination.entities.soldier.SoldierTotalHealthTest.Companion.NON_DEFAULT_SOLDIER_TOTAL_HEALTH
import domination.entities.soldier.SoldierTypeTest.Companion.DEFAULT_SOLDIER_TYPE
import domination.entities.soldier.SoldierTypeTest.Companion.NON_DEFAULT_SOLDIER_TYPE
import domination.fixtures.meleeAttack

fun defaultSoldier(
    health: SoldierHealth = DEFAULT_SOLDIER_HEALTH,
    totalHealth: SoldierHealth = DEFAULT_SOLDIER_TOTAL_HEALTH,
    culture: Culture? = null,
    type: String = DEFAULT_SOLDIER_TYPE,
    abilities: List<SoldierAbility> = emptyList(),
    attackExpended: Boolean = false
): Soldier = Soldier(
    health = health,
    totalHealth = totalHealth,
    type = type,
    culture = culture,
    abilities = abilities,
    attackExpended = attackExpended
)

fun nonDefaultSoldier(
    health: SoldierHealth = NON_DEFAULT_SOLDIER_HEALTH,
    totalHealth: SoldierHealth = NON_DEFAULT_SOLDIER_TOTAL_HEALTH,
    culture: Culture? = Culture(),
    type: String = NON_DEFAULT_SOLDIER_TYPE,
    abilities: List<SoldierAbility> = listOf(meleeAttack(15)),
    attackExpended: Boolean = true
): Soldier = Soldier(
    health = health,
    totalHealth = totalHealth,
    type = type,
    culture = culture,
    abilities = abilities,
    attackExpended = attackExpended
)