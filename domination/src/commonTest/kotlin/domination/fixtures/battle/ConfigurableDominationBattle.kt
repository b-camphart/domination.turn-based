package domination.fixtures.battle

import domination.battle.Battle
import domination.battle.DominationBattle
import domination.battle.Soldier

class ConfigurableDominationBattle : DominationBattle(mapOf()) {

    override var soldierTypes: Map<String, () -> Soldier> = super.soldierTypes

    fun addSoldiers(soldiers: List<Soldier>) {
        battle = soldiers.fold(battle) { battle, soldier -> battle.withSoldier(soldier) }
    }

    fun replaceSoldier(original: Soldier, replacement: Soldier) {
        battle = Battle(battle.soldiers - original + replacement)
    }

    fun start_battle() {}

}