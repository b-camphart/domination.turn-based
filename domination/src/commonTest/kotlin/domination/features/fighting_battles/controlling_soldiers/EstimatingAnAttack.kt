package domination.features.fighting_battles.controlling_soldiers

import bdd.*
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

    }

})