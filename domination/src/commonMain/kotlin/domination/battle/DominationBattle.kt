package domination.battle

open class DominationBattle(
    open val soldierTypes: Map<String, () -> Soldier>
) {

    open var battle: Battle = Battle(soldiers = emptyList())
        protected set

    suspend fun attack(agent: Agent, attacker: Soldier, victim: Soldier) {
        val attack = attacker.abilities.first { !it.name.contains("Defense") }
        val defense = victim.abilities.find { it.name == attack.name + " Defense" }
        val newSoldiers = battle.soldiers.map {
            if (it == victim) {
                Soldier(
                    it.type,
                    defense == null || defense.strength < attack.strength,
                    it.health - if (defense == null) attack.strength else ((attack.strength / 2) - 1),
                    it.culture,
                    it.abilities
                )
            } else if (it == attacker) {
                Soldier(
                    it.type,
                    it.isDead,
                    defense?.let { SoldierHealth(it.strength / 2) } ?: it.health,
                    it.culture,
                    it.abilities
                )
            }
            else it
        }

        val allVictimsDead = newSoldiers.all { it.type != victim.type || it.isDead }

        battle = Battle(
            winner = if (allVictimsDead) agent.culture else null,
            isOver = allVictimsDead,
            soldiers = newSoldiers
        )
    }

}