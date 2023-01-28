package domination.battle

import domination.Culture
import domination.usecases.battle.*

open class DominationBattle(
    open val soldierTypes: Map<String, () -> Soldier>,
    playerCulture: Culture
) : BattleContext {

    var attackEstimate: AttackEstimate? = null
        private set

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
        AttackUseCase(this, SimulateAttack()).attack(
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
    }

    protected open val estimateAttack: Attack.Estimate = EstimateAttackUseCase(this, SimulateAttack())

    suspend fun estimateAttack(request: Attack.Request) {
        val lazyFailure = lazy { Error("Failed to estimate attack.") }
        estimateAttack.estimateAttack(request, object : Attack.Estimate.Output {
            override suspend fun presentEstimate(estimate: AttackEstimate) {
                attackEstimate = estimate
            }
            override suspend fun validationFailed(failure: Throwable) {
                lazyFailure.value.addSuppressed(failure)
            }
        })
        if (lazyFailure.isInitialized()) throw lazyFailure.value
    }

}