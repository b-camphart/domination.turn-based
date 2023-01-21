package domination.battle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class DominationBattle(
    private val scope: CoroutineScope
) {

    open var battle: Battle = Battle(soldiers = emptyList())
        protected set

    fun attack(agent: Agent, attacker: Soldier, victim: Soldier): Job {
        return scope.launch {

            val newSoldiers = battle.soldiers.map {
                if (it == victim) Soldier(victim.type, true)
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

}