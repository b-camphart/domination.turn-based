package domination.features.fighting_battles.controlling_soldiers

import bdd.*
import domination.features.fighting_battles.controlling_soldiers.AttackingAnEnemy.Companion.a_defending_enemy_soldier
import domination.features.fighting_battles.controlling_soldiers.AttackingAnEnemy.Companion.a_susceptible_enemy
import domination.features.fighting_battles.controlling_soldiers.AttackingAnEnemy.Companion.an_attacker
import domination.fixtures.*

/**
 * Before fully committing to an attack, the player wants to look at the strength of the enemy they're considering.
 * They want to see how strong the enemy is in comparison to the soldier they're going to attack with, and estimate
 * the damage that will be done both to the enemy, and to themselves.
 *
 * For both the potential victim and the attacker, the estimate details:
 * - Their health
 * - Their total health
 * - Their relative strengths for the type of attack
 * - Their estimated health after the attack
 *
 * Their healths and total healths are simply their current healths and total healths, but their estimated healths after
 * the attack are dependent on the relative strengths they each have, given the type of attack, and the current bodily
 * strength.
 */
class EstimatingAnAttack : Feature({

    "Rule: estimate shows potential damage to victim" - {

        "Scenario: estimating the potential damage to a susceptible enemy" {
            Given { an_attacker() }
                .And { a_susceptible_enemy(with = 10.health) }

            When { the(player).uses(the("Attacking".soldier)).to_estimate_an_attack_against(the("Susceptible".soldier)) }

            Then { the(estimate).should_show(the("Susceptible".soldier), at = 1.health) }
        }

        "Scenario: estimating the potential damage to an enemy with a defense" {
            Given { an_attacker() }
                .And { a_defending_enemy_soldier(with = 10.health) }

            When { the(player).uses(the("Attacking".soldier)).to_estimate_an_attack_against(the("Defending".soldier)) }

            Then { the(estimate).should_show(the("Defending".soldier), at = 6.health) }
        }

        "Scenario: estimating an attack with a damaged soldier" {
            Given { an_attacker() }
                .And { it is_at (1 / 2f).health }
            Given { a_susceptible_enemy(with = 10.health) }

            When { the(player).uses(the("Attacking".soldier)).to_estimate_an_attack_against(the("Susceptible".soldier)) }

            Then { the(estimate).should_show(the("Susceptible".soldier), at = 6.health) }
        }

        "Scenario: estimating an attack against a damaged enemy" {
            Given { an_attacker() }
            Given { a_defending_enemy_soldier(with = 20.health) }
                .And { it is_at 10.health }

            When { the(player).uses(the("Attacking".soldier)).to_estimate_an_attack_against(the("Defending".soldier)) }

            Then { the(estimate).should_show(the("Defending".soldier), at = 4.health) }
        }

    }

    "Rule: estimate shows potential damage to attacker" - {

        "Scenario: estimating an attack against an enemy with a defense" {
            Given { an_attacker(with = 10.health) }
                .And { a_defending_enemy_soldier() }

            When { the(player).uses(the("Attacking".soldier)).to_estimate_an_attack_against(the("Defending".soldier)) }

            Then { the(estimate).should_show(the("Attacking".soldier), at = 5.health) }
        }

        "Scenario: estimating an attack against a susceptible enemy" {
            Given { an_attacker(with = 10.health) }
                .And { a_susceptible_enemy() }

            When { the(player).uses(the("Attacking".soldier)).to_estimate_an_attack_against(the("Susceptible".soldier)) }

            Then { the(estimate).should_show(the("Attacking".soldier), at = 9.health) }
        }

        "Scenario: estimating an attack against a damaged enemy" {
            Given { an_attacker(with = 10.health) }
            Given { a_defending_enemy_soldier() }
                .And { it is_at (1/2f).health }

            When { the(player).uses(the("Attacking".soldier)).to_estimate_an_attack_against(the("Defending".soldier)) }

            Then { the(estimate).should_show(the("Attacking".soldier), at = 8.health) }
        }

    }

})