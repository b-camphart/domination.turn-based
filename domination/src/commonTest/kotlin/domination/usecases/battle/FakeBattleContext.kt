package domination.usecases.battle

import domination.battle.Battle
import domination.battle.BattleContext
import domination.battle.Soldier

class FakeBattleContext(
    private val initialBattle: Battle? = null
) : BattleContext {

    val soldiers = mutableListOf<Soldier>()

    private lateinit var _battle: Battle

    override suspend fun getBattle(): Battle {
        if (!::_battle.isInitialized)
            _battle = initialBattle ?: Battle(soldiers)
        return _battle
    }

    override suspend fun <T> updateBattle(update: suspend (battle: Battle, updateBattle: (Battle) -> Unit) -> T): T {
        return update(getBattle()) { _battle = it }
    }
}