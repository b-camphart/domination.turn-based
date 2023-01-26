package domination.fixtures.battle

import domination.Culture
import domination.battle.Battle
import domination.battle.DominationBattle
import domination.battle.Soldier

class ConfigurableDominationBattle(
    private val playerCulture: Culture
) : DominationBattle(mapOf(), playerCulture) {

    override var soldierTypes: Map<String, () -> Soldier> = super.soldierTypes

    fun addSoldiers(soldiers: List<Soldier>) {
        battle = soldiers.fold(battle) { battle, soldier -> battle.withSoldier(soldier) }
    }

    fun replaceSoldier(original: Soldier, replacement: Soldier) {
        battle = Battle(battle.soldiers - original + replacement, playerCulture)
    }

    fun start_battle() {}

}