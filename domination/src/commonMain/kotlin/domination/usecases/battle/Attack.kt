package domination.usecases.battle

import domination.battle.Agent
import domination.battle.Soldier
import domination.battle.SoldierAbility
import domination.battle.SoldierId

interface Attack {

    class Request(
        val agent: Agent,
        val attackerId: SoldierId,
        val victimId: SoldierId
    )

    suspend fun attack(request: Request, output: Output)

    class Result(
        val victim: Soldier,
        val attacker: Soldier
    )

    interface Output {
        suspend fun validationFailed(failure: Throwable)
        suspend fun success(result: Result)
    }

    companion object {
        fun Soldier.defensiveStrengthAgainst(attack: SoldierAbility): Double {
            val defense = abilities.find { it.isDefenseAgainst(attack) } ?: return 0.0
            return (defense.strength.toDouble() * percentageOfTotalHealth())
        }

        fun Soldier.attackStrengthOf(attackTypeName: String): Double {
            val attack = abilities.find { it.isAttackOfType(attackTypeName) } ?: return 0.0
            return attack.strength.toDouble() * percentageOfTotalHealth()
        }

        fun SoldierAbility.isAttackOfType(attackTypeName: String) =
            name.contains(attackTypeName, true) && !name.contains("defense", true)

        fun SoldierAbility.isDefenseAgainst(attack: SoldierAbility) =
            name.contains(attack.name) && name.contains("defense", true)

        fun Soldier.percentageOfTotalHealth() = (health.value.toDouble() / totalHealth.value)

    }

}