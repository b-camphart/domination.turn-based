package domination.battle

import domination.Culture

class Soldier(
    val type: String,
    val isDead: Boolean,
    val health: SoldierHealth,
    val culture: Culture?,
    val abilities: List<SoldierAbility>
) {

    fun hasAbility(ability: SoldierAbility): Boolean = abilities.contains(ability)

}