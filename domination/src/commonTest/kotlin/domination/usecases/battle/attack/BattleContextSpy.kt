package domination.usecases.battle.attack

import domination.battle.Battle
import domination.battle.BattleContext

class BattleContextSpy : BattleContext {

    var spiedUpdate: Battle? = null
        private set

    override suspend fun getBattle(): Battle = Battle()

    override suspend fun <T> updateBattle(update: suspend (battle: Battle, updateBattle: (Battle) -> Unit) -> T): T {
        return update(getBattle()) {
            spiedUpdate = it
        }
    }

}