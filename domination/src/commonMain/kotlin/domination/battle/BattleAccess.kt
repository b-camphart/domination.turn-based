package domination.battle

interface BattleAccess {
    suspend fun getBattle(): Battle
}