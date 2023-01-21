package domination.fixtures.battle

import domination.battle.Battle
import domination.battle.DominationBattle
import domination.battle.Soldier
import kotlinx.coroutines.CoroutineScope

class ConfigurableDominationBattle(
    scope: CoroutineScope
) : DominationBattle(scope) {

    fun addSoldiers(soldiers: List<Soldier>) {
        battle = Battle(battle.winner, battle.isOver, battle.soldiers + soldiers)
    }

}