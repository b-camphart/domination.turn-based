package domination.battle

import domination.Culture

class Soldier(
    val id: SoldierId = SoldierId(),
    val type: String = "",
    health: SoldierHealth = NoHealthProvided,
    totalHealth: SoldierHealth = NoHealthProvided,
    val culture: Culture? = null,
    val abilities: List<SoldierAbility> = emptyList(),
    private val attackExpended: Boolean = false
) {

    val health = health.takeUnless { it is NoHealthProvided }
        ?: totalHealth.takeUnless { it is NoHealthProvided }
        ?: SoldierHealth(10)

    val totalHealth = totalHealth.takeUnless { it is NoHealthProvided }
        ?: this.health

    val isDead: Boolean
        get() = health.value <= 0

    val canAttack: Boolean = !attackExpended && abilities.count { ! it.isDefense } > 0

    fun hasAbility(ability: SoldierAbility): Boolean = abilities.contains(ability)
    fun withAttack(attack: SoldierAbility): Soldier {
        return Soldier(id, type = type, health, totalHealth, culture, abilities = abilities + attack, attackExpended = attackExpended)
    }

    fun withoutAttack(attack: SoldierAbility): Soldier {
        return Soldier(id, type = type, health, totalHealth, culture = culture, abilities = abilities - attack, attackExpended = attackExpended)
    }

    fun withHealth(newHealth: Int): Soldier {
        return Soldier(id = id, type = type, culture = culture, health = SoldierHealth(newHealth), totalHealth = totalHealth, abilities = abilities, attackExpended = attackExpended)
    }

    fun withTotalHealth(newTotal: Int): Soldier {
        if (totalHealth.value == newTotal) return this
        return Soldier(id = id, type = type, culture = culture, health = health, totalHealth = SoldierHealth(newTotal), abilities = abilities, attackExpended = attackExpended)
    }

    fun withoutAttackExpended(): Soldier {
        if (canAttack) return this
        return Soldier(id = id, type = type, culture = culture, health = health, totalHealth = totalHealth, abilities = abilities, attackExpended = false)
    }

    fun withAttackExpended(): Soldier {
        if (! canAttack) return this
        return Soldier(id = id, type = type, culture = culture, health = health, totalHealth = totalHealth, abilities = abilities, attackExpended = true)
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Soldier) return false

        if (totalHealth != other.totalHealth) return false
        if (type != other.type) return false
        if (canAttack != other.canAttack) return false

        return id == other.id && health == other.health && culture == other.culture && abilities == other.abilities

    }

    override fun toString(): String {
        return "Soldier(" +
                "id=$id, " +
                "type='$type', " +
                "isDead=$isDead, " +
                "culture=$culture, " +
                "abilities=$abilities, " +
                "health=$health, " +
                "totalHealth=$totalHealth, " +
                "canAttack=$canAttack" +
                ")"
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + totalHealth.hashCode()
        result = 31 * result + canAttack.hashCode()
        result = 31 * result + abilities.hashCode()
        return result
    }


    private object NoHealthProvided : SoldierHealth(-1)

}