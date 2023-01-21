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

            battle = Battle(
                winner = agent.culture,
                isOver = true,
                soldiers = listOf(
                    Soldier(attacker.type, false),
                    Soldier(victim.type, true)
                )
            )

        }
    }

}