package domination.usecases.battle

import domination.battle.Battle
import domination.battle.Soldier
import domination.battle.SoldierAbility
import domination.battle.SoldierId
import domination.usecases.battle.Attack.Companion.attackStrengthOf
import domination.usecases.battle.Attack.Companion.defensiveStrengthAgainst
import domination.usecases.battle.Attack.Companion.isDefenseAgainst
import domination.usecases.battle.Attack.Companion.percentageOfTotalHealth
import kotlin.math.roundToInt

class SimulateAttack : Attack.Simulation {

    override suspend fun Battle.simulateAttack(request: Attack.Request, errorCollector: UseCaseOutput): Battle? {
        val attacker = getValidatedAttacker(request, errorCollector)
        val victim = getValidatedVictim(request, errorCollector)
        if (attacker == null || victim == null)
            return null

        val (updatedVictim, updatedAttacker) = ValidatedAttack(attacker, victim).getSoldiersAfterAttack()

        return withSoldier(updatedVictim).withSoldier(updatedAttacker)
    }

    private suspend fun Battle.getValidatedAttacker(request: Attack.Request, errorCollector: UseCaseOutput): Soldier? {
        val attacker = getSoldierById(request.attackerId, errorCollector) ?: return null
        return attacker.takeIf { attacker.isValidAttacker(request, errorCollector) }
    }

    private suspend fun Soldier.isValidAttacker(request: Attack.Request, errorCollector: UseCaseOutput): Boolean {
        return soldierIsValid(errorCollector) {
            listOfNotNull(
                if (culture != request.agent.culture) SoldierMustBeCommandedByItsOwnCulture(request.agent.culture, culture) else null,
                if (isDead) DeadSoldiersCannotAttack() else null,
                if (abilities.none { ! it.name.contains("defense", true) }) SoldierDoesNotHaveAnAttack() else null,
                if (! canAttack) SoldierHasExpendedItsAttack() else null
            )
        }
    }

    private suspend fun Battle.getValidatedVictim(request: Attack.Request, errorCollector: UseCaseOutput): Soldier? {
        val victim = getSoldierById(request.victimId, errorCollector) ?: return null
        return victim.takeIf { victim.isValidVictim(errorCollector, request) }
    }

    private suspend fun Soldier.isValidVictim(
        errorCollector: UseCaseOutput,
        request: Attack.Request
    ): Boolean {
        return soldierIsValid(errorCollector) {
            listOfNotNull(
                if (culture == request.agent.culture) CannotAttackAlliedSoldier() else null,
                if (isDead) DeadSoldiersCannotBeAttacked() else null,
            )
        }
    }

    private suspend fun Battle.getSoldierById(id: SoldierId, errorCollector: UseCaseOutput): Soldier? {
        val soldier = soldiers.find { it.id == id }
        if (soldier == null) errorCollector.validationFailed(SoldierIsNotInBattle())
        return soldier
    }

    private suspend fun soldierIsValid(errorCollector: UseCaseOutput, validator: () -> List<Throwable>): Boolean {
        val validationErrors = validator()
        validationErrors.forEach { errorCollector.validationFailed(it) }
        return validationErrors.isEmpty()
    }

}

private class ValidatedAttack(
    private val attacker: Soldier,
    private val victim: Soldier
) {

    private val attack: SoldierAbility = attacker.abilities.first { !it.name.contains("defense", true) }
    private val defense: SoldierAbility? = victim.abilities.find { it.isDefenseAgainst(attack) }

    fun getSoldiersAfterAttack(): Pair<Soldier, Soldier> {
        val updatedVictim = getAffectedVictim()
        val updatedAttacker = getAffectedAttacker()
        return Pair(updatedVictim, updatedAttacker)
    }

    private fun getAffectedVictim(): Soldier {
        val damageToVictim = getDamageDealtToVictim()
        return victim.withHealth((victim.health.value - damageToVictim.coerceAtLeast(0.0)).roundToInt())
    }

    private fun getAffectedAttacker(): Soldier {
        val damageToAttacker = getDamageDealtToAttacker()
        return attacker.withHealth((attacker.health.value.toDouble() - damageToAttacker).toInt())
            .withAttackExpended()
    }

    private fun getDamageDealtToVictim(): Double {
        val defenseStrength = victim.defensiveStrengthAgainst(attack)
        val attackStrength = attacker.attackStrengthOf(attack.name)
        return baseDamageToVictim(attackStrength, defenseStrength) - victim.defensiveBonus()
    }

    private fun getDamageDealtToAttacker(): Double {
        val defenseStrength = baseDefenseStrength(defense, victim) ?: victim.defensiveBonus().toDouble()
        return damageToAttacker(defenseStrength, attack.strength.toDouble())
    }

    private fun baseDamageToVictim(attackStrength: Double, defenseStrength: Double): Double {
        return calculateDamage(attackStrength, defenseStrength)
    }

    private fun damageToAttacker(defenseStrength: Double, attackStrength: Double): Double {
        return calculateDamage(defenseStrength, attackStrength)
    }

    private fun baseDefenseStrength(defense: SoldierAbility?, victim: Soldier): Double? {
        return if (defense == null) null else defense.strength.toDouble() * victim.percentageOfTotalHealth()
    }

    private fun Soldier.defensiveBonus() = (health.value / 10)

    private fun calculateDamage(attackStrength: Double, defenseStrength: Double): Double {
        if (attackStrength == 0.0 && defenseStrength == 0.0) return 0.0
        return attackStrength * (attackStrength / (attackStrength + defenseStrength))
    }
}