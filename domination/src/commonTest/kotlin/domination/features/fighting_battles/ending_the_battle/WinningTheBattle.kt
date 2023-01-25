package domination.features.fighting_battles.ending_the_battle

import bdd.Given
import bdd.Then
import bdd.When
import domination.fixtures.*
import io.kotest.core.spec.style.StringSpec

/**
 * To win the battle, the player must destroy all enemy units in the battle, which then ends the battle.
 */
class WinningTheBattle : StringSpec({

    "Scenario: Destroy last enemy" {
        Given { a_battle_has_been_started }
            .And { there_is(one_swordsman).in_the_battle }
            .And { it is_allied_with the(player) }
            .And { there_is(one_archer).in_the_battle }
            .And { it is_allied_with the(enemy) }
            .And { it is_at 6.health }

        When { the(player).instructs(the(swordsman)).to_attack(the(archer)) }

        Then { the(archer).should_be_dead }
            .And { the(battle).should_be_over }
            .And { the(player) should_be_the_winner_of the(battle) }
    }

    "Scenario: Destroy an enemy, but one remains" {
        Given { a_battle_has_been_started }
            .And { there_is(one_swordsman).in_the_battle }
            .And { it is_allied_with the(player) }
            .And { there_are(2.archers).in_the_battle }
            .And { the(archers) are_allied_with the(enemy) }
            .And { the(archers) are_at 6.health }

        When { the(player).instructs(the(swordsman)).to_attack(one_of(the(archers))) }

        Then { one_of(the(archers)).should_be_dead }
            .But { the(battle).should_not_be_over }
            .And { it.should_not_have_a_winner }
    }

})