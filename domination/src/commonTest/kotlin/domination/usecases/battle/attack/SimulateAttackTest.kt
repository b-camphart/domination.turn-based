package domination.usecases.battle.attack

import domination.Culture
import domination.battle.*
import domination.entities.soldier.SoldierHealthTest.Companion.DEFAULT_SOLDIER_HEALTH
import domination.entities.soldier.defaultSoldier
import domination.fixtures.*
import domination.usecases.battle.*
import domination.usecases.battle.Attack.Companion.attackStrengthOf
import domination.usecases.battle.Attack.Companion.defensiveStrengthAgainst
import io.kotest.assertions.fail
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.shouldForAtLeastOne
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf

class SimulateAttackTest : FunSpec() {

    init {
        test("attacker must be in the battle") {
            player.attacking(anEnemySoldier(), with = aNonExistentSoldier())
                .shouldFailBecause<SoldierIsNotInBattle>()
        }

        test("victim must be in the battle") {
            player.attacking(aNonExistentSoldier(), with = someSoldier(alliedWith = player))
                .shouldFailBecause<SoldierIsNotInBattle>()
        }

        test("attacker must be allied with agent to be commanded to attack") {
            player.attacking(anEnemySoldier(), with = anEnemySoldier())
                .shouldFailBecause<SoldierMustBeCommandedByItsOwnCulture>()
        }

        test("victim soldier must not be allied with agent") {
            player.attacking(anAlliedSoldier(), with = anAlliedSoldier())
                .shouldFailBecause<CannotAttackAlliedSoldier>()
        }

        test("dead soldiers cannot attack") {
            player.attacking(anEnemySoldier(), with = aDeadSoldier(alliedWith = player))
                .shouldFailBecause<DeadSoldiersCannotAttack>()
        }

        test("dead soldiers cannot be attacked") {
            player.attacking(aDeadSoldier(alliedWith = theEnemy), with = anAlliedSoldier())
                .shouldFailBecause<DeadSoldiersCannotBeAttacked>()
        }

        context("attacking soldier must have an attack") {

            test("attacker with no abilities") {
                player.attacking(anEnemySoldier(), with = anAlliedSoldier())
                    .shouldFailBecause<SoldierDoesNotHaveAnAttack>()
            }

            test("attacker with only defenses") {
                player.attacking(anEnemySoldier(), with = anAlliedSoldier(withAbility = meleeDefense(10)))
                    .shouldFailBecause<SoldierDoesNotHaveAnAttack>()
            }

        }

        test("attacking soldier must still be able to attack during this turn") {
            val theAttackingSoldier = anAlliedSoldier(withAbility = meleeAttack(10))
                .and(withoutAbilityToAttack)

            player.attacking(anEnemySoldier(), with = theAttackingSoldier)
                .shouldFailBecause<SoldierHasExpendedItsAttack>()
        }

        context("damage dealt to victim") {

            context("relative strength of defense changes the base damage") {
                val victimHealth = 10
                listOf(
                    listOf(/*attack: */ 0, /*defense: */10, /*expectedHealth: */ 10),
                    listOf(/*attack: */10, /*defense: */ 0, /*expectedHealth: */  1),
                    listOf(/*attack: */10, /*defense: */10, /*expectedHealth: */  6),
                    listOf(/*attack: */10, /*defense: */ 5, /*expectedHealth: */  4),
                ).forEach { (attack, defense, expectedHealth) ->
                    test("attack of $attack against $defense defense leaves victim with $expectedHealth health") {
                        val attacker = anAlliedSoldier(withHealthOf = 10)
                            .and(withAbility = meleeAttack(of_strength = attack))
                        val victim = anEnemySoldier(withHealthOf = victimHealth)
                            .and(withAbility = meleeDefense(of_strength = defense))
                        player.attacking(victim, with = attacker)
                            .shouldLeaveTheVictimAt(expectedHealth.health)
                    }
                }
            }

            context("victim receives defensive bonus, relative to their current health") {
                val attackStrength = 10
                listOf(
                    listOf(/*initialHealth: */10, /*defenseStrength: */ 0, /*expectedHealth: */ 1),
                    listOf(/*initialHealth: */20, /*defenseStrength: */ 0, /*expectedHealth: */12),
                    listOf(/*initialHealth: */10, /*defenseStrength: */10, /*expectedHealth: */ 6)
                ).forEach { (initialHealth, defenseStrength, expectedHealth) ->
                    test("$initialHealth health of the victim works with $defenseStrength defensive strength to reduce damage") {
                        val attacker = anAlliedSoldier(withAbility = meleeAttack(of_strength = attackStrength))
                        val victim = anEnemySoldier(withHealthOf = initialHealth)
                            .and(withAbility = meleeDefense(of_strength = defenseStrength))

                        player.attacking(victim, with = attacker)

                            .shouldLeaveTheVictimAt(expectedHealth.health)
                    }
                }
            }

            test("health of the attacker influences the overall damage that can be produced") {
                player.attacking(
                    anEnemySoldier(withHealthOf = 10)
                        .and(withAbility = meleeDefense(of_strength = 10)),
                    with = anAlliedSoldier(withHealthOf = 5)
                        .and(totalHealthOf = 10)
                        .and(withAbility = meleeAttack(of_strength = 10))
                )
                    .shouldLeaveTheVictimAt(9.health)
            }

            test("health of the victim affects its ability to defend") {
                player.attacking(
                    anEnemySoldier(withHealthOf = 10)
                        .and(totalHealthOf = 20)
                        .and(withAbility = meleeDefense(of_strength = 10)),
                    with = anAlliedSoldier(withHealthOf = 10)
                        .and(withAbility = meleeAttack(of_strength = 10))
                )
                    .shouldLeaveTheVictimAt(4.health)
            }

        }

        context("damage dealt to attacker") {

            test("victim has no defensive ability, still does damage based on victim's overall health") {
                val victim =  anEnemySoldier(withHealthOf = 10)
                val attacker = anAlliedSoldier(withHealthOf = 10)
                    .and(withAbility = meleeAttack(0))

                player.attacking(victim, with = attacker)
                    .shouldLeaveTheAttackerAt(9.health)
            }

            test("victim has no defensive ability, still does damage based on victim's overall health") {
                val victim =  anEnemySoldier(withHealthOf = 20)
                val attacker = anAlliedSoldier(withHealthOf = 10)
                    .and(withAbility = meleeAttack(0))

                player.attacking(victim, with = attacker)
                    .shouldLeaveTheAttackerAt(8.health)
            }

            test("attacker is damaged based on strength of victim's defensive strength") {
                val victim =  anEnemySoldier(withHealthOf = 10)
                    .and(withAbility = meleeDefense(10))
                val attacker = anAlliedSoldier(withHealthOf = 10)
                    .and(withAbility = meleeAttack(10))

                player.attacking(victim, with = attacker)
                    .shouldLeaveTheAttackerAt(5.health)
            }

            test("attacker is damaged based on strength of victim's defensive strength") {
                val victim =  anEnemySoldier(withHealthOf = 10)
                    .and(withAbility = meleeDefense(10))
                val attacker = anAlliedSoldier(withHealthOf = 10)
                    .and(withAbility = meleeAttack(5))

                player.attacking(victim, with = attacker)
                    .shouldLeaveTheAttackerAt(3.health)
            }

            test("damaged victims cannot defend as well and thus deal less damage to attacker") {
                val attacker = anAlliedSoldier(withHealthOf = 10)
                    .and(withAbility = meleeAttack(10))
                val victim = anEnemySoldier(withHealthOf = 10)
                    .and(totalHealthOf = 20)
                    .and(withAbility = meleeDefense(10))

                player.attacking(victim, with = attacker)
                    .shouldLeaveTheAttackerAt(8.health)
            }

            test("damaged victims cannot defend as well and thus deal less damage to attacker") {
                val attacker = anAlliedSoldier(withHealthOf = 10)
                    .and(withAbility = meleeAttack(10))
                val victim = anEnemySoldier(withHealthOf = 10)
                    .and(totalHealthOf = 15)
                    .and(withAbility = meleeDefense(10))

                player.attacking(victim, with = attacker)
                    .shouldLeaveTheAttackerAt(7.health)
            }

        }

        context("strength of defense") {

            test("victim with no ability to defend against the attack type has a defense strength of 0") {
                defaultSoldier(abilities = emptyList())
                    .defensiveStrengthAgainst(meleeAttack(of_strength = 10))
                    .shouldBe(0)

                defaultSoldier(abilities = listOf(SoldierAbility(name = "OTHER", strength = 6)))
                    .defensiveStrengthAgainst(meleeAttack(of_strength = 10))
                    .shouldBe(0)

                defaultSoldier(abilities = listOf(rangedDefense(of_strength = 6)))
                    .defensiveStrengthAgainst(meleeAttack(of_strength = 10))
                    .shouldBe(0)
            }

            test("victim with defense and full health has full defense strength against attack") {
                defaultSoldier(abilities = listOf(meleeDefense(of_strength = 5)))
                    .defensiveStrengthAgainst(meleeAttack(of_strength = 10))
                    .shouldBe(5)

                defaultSoldier(abilities = listOf(meleeDefense(of_strength = 10)))
                    .defensiveStrengthAgainst(meleeAttack(of_strength = 10))
                    .shouldBe(10)
            }

            test("weakened victim cannot fully defend against attack") {
                defaultSoldier(
                    health = 10.health,
                    totalHealth = 20.health,
                    abilities = listOf(meleeDefense(of_strength = 5))
                )
                    .defensiveStrengthAgainst(meleeAttack(of_strength = 10))
                    .shouldBe(2.5)

                defaultSoldier(
                    health = 5.health,
                    totalHealth = 20.health,
                    abilities = listOf(meleeDefense(of_strength = 5))
                )
                    .defensiveStrengthAgainst(meleeAttack(of_strength = 10))
                    .shouldBe(1.25)
            }

        }

        context("strength of attack") {

            test("attacker with full health attacks with full strength") {
                defaultSoldier(abilities = listOf(meleeAttack(of_strength = 10)))
                    .attackStrengthOf("melee")
                    .shouldBe(10)
                defaultSoldier(abilities = listOf(meleeAttack(of_strength = 20)))
                    .attackStrengthOf("melee")
                    .shouldBe(20)
            }

            test("selects attack from soldier's abilities based on provided name") {
                defaultSoldier(abilities = listOf(meleeAttack(of_strength = 20), rangedAttack(of_strength = 15)))
                    .attackStrengthOf("ranged")
                    .shouldBe(15)
                defaultSoldier(abilities = listOf(rangedAttack(of_strength = 15), meleeAttack(of_strength = 20)))
                    .attackStrengthOf("ranged")
                    .shouldBe(15)
                defaultSoldier(abilities = listOf(rangedDefense(of_strength = 20), rangedAttack(of_strength = 15)))
                    .attackStrengthOf("ranged")
                    .shouldBe(15)
            }

            test("weakened attacker does not attack with full strength") {
                defaultSoldier(
                    health = SoldierHealth(10),
                    totalHealth = SoldierHealth(20),
                    abilities = listOf(meleeAttack(of_strength = 10))
                )
                    .attackStrengthOf("melee")
                    .shouldBe(5)

                defaultSoldier(
                    health = SoldierHealth(10),
                    totalHealth = SoldierHealth(20),
                    abilities = listOf(meleeAttack(of_strength = 20))
                )
                    .attackStrengthOf("melee")
                    .shouldBe(10)

                defaultSoldier(
                    health = SoldierHealth(5),
                    totalHealth = SoldierHealth(20),
                    abilities = listOf(meleeAttack(of_strength = 20))
                )
                    .attackStrengthOf("melee")
                    .shouldBe(5)
            }

            test("attacker without attack does no damage") {
                defaultSoldier()
                    .attackStrengthOf("melee")
                    .shouldBe(0)

                defaultSoldier(abilities = listOf(rangedAttack(of_strength = 10)))
                    .attackStrengthOf("melee")
                    .shouldBe(0)
            }

        }

        test("attacker expends their attack for the turn") {
            val attacker = anAlliedSoldier()
                .and(withAbility = meleeAttack(0))

            player.attacking(anEnemySoldier(), with = attacker)
                .shouldExpendTheAttackersAttack()
        }

    }

    private val player: Agent = Agent(Culture())
    private val theEnemy: Agent = Agent(Culture())

    private val soldiers = mutableListOf<Soldier>()

    private val context = BattleAccess { Battle(soldiers) }

    private fun aNonExistentSoldier() = Soldier()
    private fun anEnemySoldier(withHealthOf: Int = 10): Soldier {
        val soldier = defaultSoldier(health = withHealthOf.health, culture = null)
        soldiers += soldier
        return soldier
    }

    private fun anAlliedSoldier(
        withHealthOf: Int = DEFAULT_SOLDIER_HEALTH.value,
        withAbility: SoldierAbility? = null
    ): Soldier {
        val soldier = defaultSoldier(
            health = withHealthOf.health,
            culture = player.culture,
            abilities = listOfNotNull(withAbility)
        )
        soldiers += soldier
        return soldier
    }

    private fun Soldier.and(withAbility: SoldierAbility): Soldier {
        val soldier = withAttack(withAbility)
        soldiers -= this
        soldiers += soldier
        return soldier
    }

    private fun Soldier.and(totalHealthOf: Int): Soldier {
        val soldier = withTotalHealth(totalHealthOf)
        soldiers -= this
        soldiers += soldier
        return soldier
    }

    private fun Soldier.and(canAttack: CanAttack): Soldier {
        val soldier = when (canAttack) {
            CanAttack.Yes -> withoutAttackExpended()
            CanAttack.No -> withAttackExpended()
        }
        soldiers -= this
        soldiers += soldier
        return soldier
    }

    private val withoutAbilityToAttack get() = CanAttack.No

    private enum class CanAttack { Yes, No }

    private fun aDeadSoldier(alliedWith: Agent? = null): Soldier {
        val soldier = defaultSoldier(health = 0.health, culture = alliedWith?.culture)
        soldiers += soldier
        return soldier
    }

    private fun someSoldier(alliedWith: Agent? = null): Soldier {
        val soldier = defaultSoldier(culture = alliedWith?.culture)
        soldiers += soldier
        return soldier
    }

    private suspend fun Agent.attacking(victim: Soldier, with: Soldier): OutputSpy {
        val agent = this
        val outputSpy = OutputSpy(attackerId = with.id, victimId = victim.id)
        outputSpy.simulatedBattle(with(SimulateAttack()) {
            context.getBattle().simulateAttack(
                Attack.Request(agent = agent, attackerId = with.id, victimId = victim.id),
                errorCollector = outputSpy
            )
        })
        return outputSpy
    }

    private inline fun <reified T : Throwable> OutputSpy.shouldFailBecause() {
        withClue("should have failed with ${T::class.simpleName}") {
            validationErrors.shouldForAtLeastOne {
                it.shouldBeTypeOf<T>()
            }
        }
    }

    private infix fun OutputSpy.shouldLeaveTheVictimAt(expectedHealth: SoldierHealth) {
        val result = result ?: fail("No result received.  $validationErrors")
        withClue("victim in result should have expected health") {
            result.getSoldier(victimId)!!.health shouldBe expectedHealth
        }
    }

    private infix fun OutputSpy.shouldLeaveTheAttackerAt(expectedHealth: SoldierHealth) {
        val result = result ?: fail("No result received.  $validationErrors")
        withClue("attacker in result should have expected health") {
            result.getSoldier(attackerId)!!.health shouldBe expectedHealth
        }
    }

    private fun OutputSpy.shouldExpendTheAttackersAttack() {
        val result = result ?: fail("No result received.  $validationErrors")
        withClue("attacker in result should have expended their attack") {
            result.getSoldier(attackerId)!!.canAttack.shouldBeFalse()
        }
    }

    class OutputSpy(
        val attackerId: SoldierId,
        val victimId: SoldierId
    ) : UseCaseOutput {
        val validationErrors = mutableListOf<Throwable>()
        var result: Battle? = null
            private set

        override suspend fun validationFailed(failure: Throwable) {
            validationErrors.add(failure)
        }

        fun simulatedBattle(battle: Battle?) {
            result = battle
        }

    }

}