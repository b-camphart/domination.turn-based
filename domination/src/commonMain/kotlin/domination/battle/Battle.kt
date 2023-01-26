package domination.battle

import domination.Culture

class Battle private constructor(
    private val soldiersById: Map<SoldierId, Soldier>,
    private val playerCulture: Culture
) {

    constructor(
        soldiers: List<Soldier> = emptyList(),
        playerCulture: Culture = Culture()
    ) : this(soldiers.associateBy { it.id }, playerCulture)

    val soldiers by lazy { soldiersById.values.toList() }

    val winner: Culture? by lazy {
            val firstLivingCulture = soldiersById.values.find { ! it.isDead }?.culture
            if (soldiersById.all { it.value.isDead || it.value.culture == firstLivingCulture })
                firstLivingCulture
            else null
        }

    val isOver: Boolean by lazy {
        if (soldiersById.isEmpty()) return@lazy true
        val firstLivingSoldier = soldiersById.values.find { !it.isDead } ?: return@lazy true
        val firstCulture = firstLivingSoldier.culture
        soldiersById.values.all { it.isDead || it.culture == firstCulture }
                || soldiersById.none { !it.value.isDead && it.value.culture == playerCulture }
    }

    fun withSoldier(soldier: Soldier): Battle {
        return Battle(soldiersById = soldiersById + (soldier.id to soldier), playerCulture)
    }
}