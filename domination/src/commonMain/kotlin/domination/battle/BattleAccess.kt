package domination.battle

fun interface BattleAccess {
    suspend fun getBattle(): Battle
}