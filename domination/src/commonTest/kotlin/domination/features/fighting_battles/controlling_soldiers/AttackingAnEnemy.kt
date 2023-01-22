package domination.features.fighting_battles.controlling_soldiers

import io.kotest.core.spec.style.StringSpec
import bdd.*
import domination.fixtures.*

/**
 * Attacking an enemy involves the soldier using either their primary attack, or one of their secondary attacks
 * (if they have any), against an enemy soldier.  The enemy soldier may have a defense against the attack, or
 * they may be susceptible to that form of attack.  The enemy soldier's health will drop based on how strong the
 * attacking soldier is.  The attacking soldier's health will drop based on how well the enemy soldier defends
 * against the attack.  The health of each soldier impacts their relative attack or defense strengths.
 */
class AttackingAnEnemy : StringSpec({

    "Scenario: victim can defend against the attack" {
        Given { a_soldier_with_a(meleeAttack(of_strength = 10)) }
            .And { it has 10.health }
            .And { it is_called_a "Attacker" }
            .And { it has_been_added_to the(battle) }
            .And { it is_allied_with the(player) }
        Given { a_soldier_with_a(meleeDefense(of_strength = 10)) }
            .And { it has 10.health }
            .And { it is_called_a "Defender" }
            .And { it has_been_added_to the(battle) }
            .And { it is_allied_with the(enemy) }

        When { the(player).instructs(the("Attacker".soldier)).to_attack(the("Defender".soldier)) }

        Then { the("Attacker".soldier) should_have 5.health }
            .And { the("Defender".soldier) should_have 6.health }

    }

    "Scenario: victim is susceptible to attack" {
        Given { a_soldier_with_a(meleeAttack(of_strength = 10)) }
            .And { it has 10.health }
            .And { it is_called_a "Attacker" }
            .And { it has_been_added_to the(battle) }
            .And { it is_allied_with the(player) }
        Given { a_soldier_called("Susceptible") }
            .And { it has 15.health }
            .And { it has_been_added_to the(battle) }
            .And { it is_allied_with the(enemy) }

        When { the(player).instructs(the("Attacker".soldier)).to_attack(the("Susceptible".soldier)) }

        Then { the("Attacker".soldier) should_have 10.health }
            .And { the("Susceptible".soldier) should_have 5.health }
    }

})