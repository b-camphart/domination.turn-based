package domination.fixtures.battle

import domination.battle.Battle
import domination.battle.DominationBattle
import domination.battle.Soldier

class ConfigurableDominationBattle : DominationBattle(mapOf()) {

    override var soldierTypes: Map<String, () -> Soldier> = super.soldierTypes

    fun addSoldiers(soldiers: List<Soldier>) {
        battle = Battle(battle.winner, battle.isOver, battle.soldiers + soldiers)
    }

    fun replaceSoldier(original: Soldier, replacement: Soldier) {
        battle = Battle(battle.winner, battle.isOver, battle.soldiers - original + replacement)
    }

    fun start_battle() {}

}