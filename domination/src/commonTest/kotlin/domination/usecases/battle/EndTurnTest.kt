package domination.usecases.battle

import domination.Culture
import domination.battle.Battle
import domination.battle.Soldier
import domination.battle.SoldierId
import domination.entities.soldier.defaultSoldier
import domination.fixtures.health
import domination.fixtures.ids
import domination.fixtures.meleeAttack
import io.kotest.assertions.fail
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import kotlin.reflect.KClass

class EndTurnTest : FunSpec({

    test("culture must be in the battle to end their turn") {
        Culture().endingTheirTurnDuring(anOngoingBattle())
            .shouldFailBecause<CultureNotInBattle>()
    }

    test("culture must have at least one living soldier allied with them to end their turn") {
        val culture = Culture()
        val battle = anOngoingBattle(withSoldiers = listOf(defaultSoldier(health = 0.health, culture = culture)))
        culture.endingTheirTurnDuring(battle)
            .shouldFailBecause<CultureHasLost>()
    }

    test("battle cannot be over for culture to end their turn") {
        val culture = Culture()
        val battle = Battle(soldiers = listOf(defaultSoldier(health = 10.health, culture = culture)))
        culture.endingTheirTurnDuring(battle)
            .shouldFailBecause<BattleHasEnded>()
    }

    test("expended soldiers are reset to allow attacks in next turn") {
        val culture = Culture()
        val culturesSoldiers = listOf(defaultSoldier(culture = culture, abilities = listOf(meleeAttack()), attackExpended = true))
        val battle = anOngoingBattle(withSoldiers = culturesSoldiers)
        culture.endingTheirTurnDuring(battle)
            .shouldResetSoldiersExpenditure(culturesSoldiers.ids)
    }

    test("should only affect allied soldiers") {
        val culture = Culture()
        val culturesSoldiers = listOf(defaultSoldier(culture = culture, abilities = listOf(meleeAttack())))
        val battle = anOngoingBattle(withSoldiers = culturesSoldiers)
        culture.endingTheirTurnDuring(battle)
            .shouldNotResetSoldiersNotAlliedWith(culture)
    }

}) {

    companion object {

        private fun anOngoingBattle(withSoldiers: List<Soldier> = emptyList()): Battle {
            val playerCulture = Culture()
            return Battle(
                playerCulture = playerCulture,
                soldiers = listOf(
                    defaultSoldier(health = 10.health, culture = Culture()),
                    defaultSoldier(health = 10.health, culture = playerCulture)
                ) + withSoldiers
            )
        }

        private suspend fun Culture.endingTheirTurnDuring(battle: Battle): OutputSpy {
            val context = FakeBattleContext(battle)
            val useCase: EndTurn = EndTurnUseCase(context)
            val outputSpy = OutputSpy(battle)
            useCase.endTurn(culture = this, output = outputSpy)
            outputSpy.resultingBattle = context.getBattle()
            return outputSpy
        }

        private fun OutputSpy.shouldResetSoldiersExpenditure(ids: Set<SoldierId>) {
            val result = result ?: fail("No result received.  Errors:\n$validationErrors")
            val resultingBattle = resultingBattle!!
            ids.forEach { id ->
                val resetSoldier = result.resetSoldiers.single { it.id == id }
                resetSoldier.canAttack.shouldBeTrue()
                resultingBattle.getSoldier(id)!!.canAttack.shouldBeTrue()
            }
        }

        private fun OutputSpy.shouldNotResetSoldiersNotAlliedWith(culture: Culture) {
            val result = result ?: fail("No result received.  Errors:\n$validationErrors")
            val resultingBattle = resultingBattle!!
            withClue("soldiers not allied with $culture should not be in output") {
                result.resetSoldiers.none { it.culture != culture }.shouldBeTrue()
            }
            withClue("soldiers not allied with $culture should not have been updated in battle") {
                initialBattle.soldiers.filter { it.culture != culture }
                    .all { it == resultingBattle.getSoldier(it.id) }.shouldBeTrue()
            }
        }

        private inline fun <reified T : Throwable> OutputSpy.shouldFailBecause() =
            shouldFailBecause(T::class)

        private fun <T : Throwable> OutputSpy.shouldFailBecause(type: KClass<T>) {
            validationErrors.find(type::isInstance)
                ?: fail("should have failed with ${type.simpleName}.  Errors received:\n$validationErrors")
            result.shouldBeNull()
        }
    }

    private class OutputSpy(val initialBattle: Battle) : EndTurn.Output {
        val validationErrors = mutableListOf<Throwable>()
        var result: EndTurn.Result? = null
        var resultingBattle: Battle? = null

        override suspend fun validationFailed(failure: Throwable) {
            validationErrors.add(failure)
        }

        override suspend fun turnEnded(result: EndTurn.Result) {
            this.result = result
        }
    }

}