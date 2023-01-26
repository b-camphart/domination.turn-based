package domination.battle

import domination.Culture
import domination.usecases.battle.Attack
import domination.usecases.battle.AttackUseCase

open class DominationBattle(
    open val soldierTypes: Map<String, () -> Soldier>,
    playerCulture: Culture
) : BattleContext {

    open var battle: Battle = Battle(soldiers = emptyList(), playerCulture = playerCulture)
        protected set

    override suspend fun getBattle(): Battle {
        return battle
    }

    override suspend fun <T> updateBattle(update: suspend (battle: Battle, updateBattle: (Battle) -> Unit) -> T): T {
        return update(battle) { battle = it }
    }

    suspend fun attack(agent: Agent, attacker: Soldier, victim: Soldier) {
        val failures = mutableListOf<Throwable>()
        AttackUseCase(this).attack(
            Attack.Request(
                agent,
                attacker.id,
                victim.id
            ),
            object : Attack.Output {
                override suspend fun success(result: Attack.Result) {
                }

                override suspend fun validationFailed(failure: Throwable) {
                    failures.add(failure)
                }
            }
        )
        if (failures.isNotEmpty()) {
            failures.forEach { it.printStackTrace() }
            error("could not attack")
        }
//        val attack = attacker.abilities.first { !it.name.contains("Defense") }
//        val defense = victim.abilities.find { it.name == attack.name + " Defense" }
//        val newSoldiers = battle.soldiers.map {
//            if (it == victim) {
//                Soldier(
//                    it.id,
//                    it.type,
//                    defense == null || defense.strength < attack.strength,
//                    it.health - if (defense == null) attack.strength else ((attack.strength / 2) - 1),
//                    it.totalHealth,
//                    it.culture,
//                    it.abilities
//                )
//            } else if (it == attacker) {
//                Soldier(
//                    it.id,
//                    it.type,
//                    it.isDead,
//                    defense?.let { SoldierHealth(it.strength / 2) } ?: it.health,
//                    it.totalHealth,
//                    it.culture,
//                    it.abilities
//                )
//            }
//            else it
//        }
//
//        val allVictimsDead = newSoldiers.all { it.type != victim.type || it.isDead }
//
//        battle = Battle(
//            winner = if (allVictimsDead) agent.culture else null,
//            isOver = allVictimsDead,
//            soldiers = newSoldiers
//        )
    }

}