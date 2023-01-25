package domination.usecases.battle

import domination.battle.*
import domination.usecases.battle.Attack.Companion.attackStrengthOf
import domination.usecases.battle.Attack.Companion.defensiveStrengthAgainst
import domination.usecases.battle.Attack.Companion.isDefenseAgainst
import domination.usecases.battle.Attack.Companion.percentageOfTotalHealth
import kotlin.math.roundToInt

class AttackUseCase(
    private val battleContext: BattleContext
) : Attack {

    override suspend fun attack(request: Attack.Request, output: Attack.Output) {
        val result = battleContext.updateBattle { battle, updateBattle ->
            val attacker = battle.getValidatedAttacker(request, output)
            val victim = battle.getValidatedVictim(request, output)
            if (attacker == null || victim == null)
                return@updateBattle null

            val (updatedVictim, updatedAttacker) = getSoldiersAfterAttack(attacker, victim)

            updateBattle(battle.withSoldier(updatedVictim).withSoldier(updatedAttacker))
            Attack.Result(updatedVictim, updatedAttacker)
        }

        if (result != null) output.success(result)
    }

    private fun getSoldiersAfterAttack(
        attacker: Soldier,
        victim: Soldier
    ): Pair<Soldier, Soldier> {
        val attack = attacker.abilities.first { !it.name.contains("defense", true) }
        val defense = victim.abilities.find { it.isDefenseAgainst(attack) }

        val damageToVictim = getDamageDealtToVictim(victim, attack, attacker)
        val damageToAttacker = getDamageDealtToAttacker(defense, victim, attack)

        val updatedVictim = victim.withHealth((victim.health.value - damageToVictim.coerceAtLeast(0.0)).roundToInt())
        val updatedAttacker = attacker.withHealth((attacker.health.value.toDouble() - damageToAttacker).toInt())
        return Pair(updatedVictim, updatedAttacker)
    }

    private fun getDamageDealtToVictim(
        victim: Soldier,
        attack: SoldierAbility,
        attacker: Soldier
    ): Double {
        val defenseStrength = victim.defensiveStrengthAgainst(attack)
        val attackStrength = attacker.attackStrengthOf(attack.name)
        return baseDamageToVictim(attackStrength, defenseStrength) - victim.defensiveBonus()
    }

    private fun getDamageDealtToAttacker(
        defense: SoldierAbility?,
        victim: Soldier,
        attack: SoldierAbility
    ): Double {
        val defenseStrength = baseDefenseStrength(defense, victim) ?: victim.defensiveBonus().toDouble()
        return damageToAttacker(defenseStrength, attack.strength.toDouble())
    }

    private fun baseDefenseStrength(defense: SoldierAbility?, victim: Soldier): Double? {
        return if (defense == null) null else defense.strength.toDouble() * victim.percentageOfTotalHealth()
    }

    private fun Soldier.defensiveBonus() = (health.value / 10)

    private suspend fun Battle.getValidatedAttacker(request: Attack.Request, output: Attack.Output): Soldier? {
        val attacker = getSoldierById(request.attackerId, output) ?: return null
        return attacker.takeIf { attacker.isValidAttacker(request, output) }
    }

    private suspend fun Soldier.isValidAttacker(request: Attack.Request, output: Attack.Output): Boolean {
        return soldierIsValid(output) {
            listOfNotNull(
                if (culture != request.agent.culture) SoldierMustBeCommandedByItsOwnCulture(request.agent.culture, culture) else null,
                if (isDead) DeadSoldiersCannotAttack() else null,
                if (abilities.none { ! it.name.contains("defense", true) }) SoldierDoesNotHaveAnAttack() else null
            )
        }
    }

    private suspend fun Battle.getValidatedVictim(request: Attack.Request, output: Attack.Output): Soldier? {
        val victim = getSoldierById(request.victimId, output) ?: return null
        return victim.takeIf { victim.isValidVictim(output, request) }
    }

    private suspend fun Soldier.isValidVictim(
        output: Attack.Output,
        request: Attack.Request
    ): Boolean {
        return soldierIsValid(output) {
            listOfNotNull(
                if (culture == request.agent.culture) CannotAttackAlliedSoldier() else null,
                if (isDead) DeadSoldiersCannotBeAttacked() else null,
            )
        }
    }

    private suspend fun Battle.getSoldierById(id: SoldierId, output: Attack.Output): Soldier? {
        val soldier = soldiers.find { it.id == id }
        if (soldier == null) output.validationFailed(SoldierIsNotInBattle())
        return soldier
    }

    private suspend fun soldierIsValid(output: Attack.Output, validator: () -> List<Throwable>): Boolean {
        val validationErrors = validator()
        validationErrors.forEach { output.validationFailed(it) }
        return validationErrors.isEmpty()
    }

    private fun baseDamageToVictim(attackStrength: Double, defenseStrength: Double): Double {
        return calculateDamage(attackStrength, defenseStrength)
    }

    private fun damageToAttacker(defenseStrength: Double, attackStrength: Double): Double {
        return calculateDamage(defenseStrength, attackStrength)
    }

    private fun calculateDamage(attackStrength: Double, defenseStrength: Double): Double {
        if (attackStrength == 0.0 && defenseStrength == 0.0) return 0.0
        return attackStrength * (attackStrength / (attackStrength + defenseStrength))
    }

}
