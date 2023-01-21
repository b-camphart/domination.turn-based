package domination.battle

interface BattleContext : BattleAccess {
    /**
     * Returning `null` within [update] signifies that no update should take place
     */
    suspend fun updateBattle(update: (Battle) -> Battle?)

}