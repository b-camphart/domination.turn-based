package domination.features.fighting_battles.taking_turns

import bdd.*
import domination.features.fighting_battles.controlling_soldiers.AttackingAnEnemy.Companion.a_defending_enemy_soldier
import domination.features.fighting_battles.controlling_soldiers.AttackingAnEnemy.Companion.an_attacker
import domination.fixtures.*

/**
 * Each soldier can only attack once per turn.
 */
class EndingASoldiersTurn : Feature({

    "Rule: A soldier may only attack once per turn" - {

        "Scenario: Soldier has already attacked during this turn" {
            Given { an_attacker() }
                .And { a_defending_enemy_soldier() }

            When { the(player).instructs(the("Attacking".soldier)).to_attack(the("Defending".soldier)) }

            Then { the("Attacking".soldier).should_not_be_able_to_attack }
        }

        "Scenario: A new turn has started" {
            Given { an_attacker() }
                .And { a_defending_enemy_soldier() }
                .And { the(player).has_instructed(the("Attacking".soldier)).to_attack(the("Defending".soldier)) }

            When { the(player).ends_their_turn }

            Then { the("Attacking".soldier).should_be_able_to_attack }
        }

    }

})