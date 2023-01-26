package domination.features.fighting_battles.controlling_soldiers

import bdd.*
import domination.fixtures.*

/**
 * To kill an enemy soldier, the enemy soldier's health must drop to zero.  This happens through attacks.
 */
class KillingAnEnemy : Feature({

    "Scenario: Attack a weak enemy" {
        Given { a_battle_has_been_started }
            .And { there_is(one_swordsman).in_the_battle }
            .And { it is_allied_with the(player) }
            .And { there_is(one_archer).in_the_battle }
            .And { it is_allied_with the(enemy) }
            .And { it is_at 6.health }

        When { the(player).instructs(the(swordsman)).to_attack(the(archer)) }

        Then { the(archer).should_be_dead }
    }

    "Scenario: Attack a strong enemy" {
        Given { a_battle_has_been_started }
            .And { there_is(one_swordsman).in_the_battle }
            .And { it is_allied_with the(player) }
        Given { there_is(one_swordsman).in_the_battle }
            .And { it is_allied_with the(enemy) }

        When { the(player).instructs(the(player.swordsman)).to_attack(the(enemy.swordsman)) }

        Then { the(enemy.swordsman).should_not_be_dead }
    }

})