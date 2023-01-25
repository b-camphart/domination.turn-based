package domination.features.fighting_battles.controlling_soldiers

import bdd.*
import domination.battle.SoldierHealth
import domination.fixtures.*
import domination.fixtures.battle.soldier.SoldierArrangements
import io.kotest.core.spec.style.FreeSpec

/**
 * Attacking an enemy involves the soldier using either their primary attack, or one of their secondary attacks
 * (if they have any), against an enemy soldier.  The enemy soldier may have a defense against the attack, or
 * they may be susceptible to that form of attack.  The enemy soldier's health will drop based on how strong the
 * attacking soldier is.  The attacking soldier's health will drop based on how well the enemy soldier defends
 * against the attack.  The health of each soldier impacts their relative attack or defense strengths.
 */
class AttackingAnEnemy : FreeSpec({

    "Rule: attacks deal damage to the victim" - {

        "Scenario: attacking a susceptible enemy" {
            Given { an_attacker() }
                .And { a_susceptible_enemy(with = 10.health) }

            When { the(player).instructs(the("Attacking".soldier)).to_attack(the("Susceptible".soldier)) }

            Then { the("Susceptible".soldier) should_have 1.health }
        }

        "Scenario: attacking an enemy with a defense" {
            Given { an_attacker() }
                .And { a_defending_enemy_soldier(with = 10.health) }

            When { the(player).instructs(the("Attacking".soldier)).to_attack(the("Defending".soldier)) }

            Then { the("Defending".soldier) should_have 6.health }
        }

        "Scenario: attacking with a damaged soldier" {
            Given { an_attacker() }
                .And { it is_at (1 / 2f).health }
            Given { a_susceptible_enemy(with = 10.health) }

            When { the(player).instructs(the("Attacking".soldier)).to_attack(the("Susceptible".soldier)) }

            Then { the("Susceptible".soldier) should_have 6.health }
        }

        "Scenario: attacking a damaged enemy" {
            Given { an_attacker() }
            Given { a_defending_enemy_soldier(with = 20.health) }
                .And { it is_at 10.health }

            When { the(player).instructs(the("Attacking".soldier)).to_attack(the("Defending".soldier)) }

            Then { the("Defending".soldier) should_have 4.health }
        }
    }

    "Rule: attacks damage the attacker" - {

        "Scenario: attacking an enemy with a defense" {
            Given { an_attacker(with = 10.health) }
                .And { a_defending_enemy_soldier() }

            When { the(player).instructs(the("Attacking".soldier)).to_attack(the("Defending".soldier)) }

            Then { the("Attacking".soldier) should_have 5.health }
        }

        "Scenario: attacking a susceptible enemy" {
            Given { an_attacker(with = 10.health) }
                .And { a_susceptible_enemy() }

            When { the(player).instructs(the("Attacking".soldier)).to_attack(the("Susceptible".soldier)) }

            Then { the("Attacking".soldier) should_have 9.health }
        }

        "Scenario: attacking a damaged enemy" {
            Given { an_attacker(with = 10.health) }
            Given { a_defending_enemy_soldier() }
                .And { it is_at (1/2f).health }

            When { the(player).instructs(the("Attacking".soldier)).to_attack(the("Defending".soldier)) }

            Then { the("Attacking".soldier) should_have 8.health }
        }
    }

}) {

    companion object {
        fun GivenScope.an_attacker(with: SoldierHealth = 10.health): SoldierArrangements {
            return a_soldier_with_a(meleeAttack(of_strength = 10))
                .has(with)
                .is_called_a("Attacking")
                .has_been_added_to(the(battle))
                .is_allied_with(the(player))
        }

        fun GivenScope.a_susceptible_enemy(with: SoldierHealth = 10.health): SoldierArrangements {
            return  a_soldier_called_a("Susceptible")
                .has(with)
                .has_been_added_to(the(battle))
                .is_allied_with(the(enemy))
        }

        fun GivenScope.a_defending_enemy_soldier(with: SoldierHealth = 20.health): SoldierArrangements {
            return a_soldier_called_a("Defending")
                .has_a(meleeDefense(of_strength = 10))
                .has(with)
                .has_been_added_to(the(battle))
                .is_allied_with(the(enemy))
        }
    }

}