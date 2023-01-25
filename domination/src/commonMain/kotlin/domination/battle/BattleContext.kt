package domination.battle

interface BattleContext : BattleAccess {
    suspend fun <T> updateBattle(update: suspend (battle: Battle, updateBattle: (Battle) -> Unit) -> T) : T

}