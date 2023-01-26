package domination.features.fighting_battles.ending_the_battle

import bdd.*
import domination.fixtures.*
/**
 * To lose the battle, all the player's soldiers must be destroyed.  Even if multiple other cultures' soldiers are still
 * alive.  Once the player loses their last soldier, the battle is over.
 */
class LosingTheBattle : Feature({

    "Scenario: Player's last soldier is destroyed" {
        Given { a_battle_has_been_started }
            .And { there_is(one_archer).in_the_battle }
            .And { it is_allied_with the(player) }
            .And { it is_at 6.health }
            .And { there_is(one_swordsman).in_the_battle }
            .And { it is_allied_with the(enemy) }

        When { the(enemy).instructs(the(swordsman)).to_attack(the(archer)) }

        Then { the(archer).should_be_dead }
            .And { the(battle).should_be_over }
            .And { the(enemy) should_be_the_winner_of the(battle) }
    }

    "Scenario: Two other cultures remain, but last player soldier is destroyed" {
        Given { a_battle_has_been_started }
            .And { there_is(one_archer).in_the_battle }
            .And { it is_allied_with the(player) }
            .And { it is_at 6.health }
            .And { there_is(one_swordsman).in_the_battle }
            .And { it is_allied_with the(enemy) }
            .And { there_is(one_swordsman).in_the_battle }
            .And { it is_allied_with the(other_enemy) }

        When { the(enemy).instructs(the(enemy.swordsman)).to_attack(the(archer)) }

        Then { the(archer).should_be_dead }
            .And { the(battle).should_be_over }
            .But { the(battle).should_not_have_a_winner }
    }

})