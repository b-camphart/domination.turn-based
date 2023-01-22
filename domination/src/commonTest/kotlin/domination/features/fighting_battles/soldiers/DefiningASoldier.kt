package domination.features.fighting_battles.soldiers

import bdd.Given
import bdd.Then
import bdd.When
import domination.battle.Soldier
import domination.battle.SoldierHealth
import domination.fixtures.*
import io.kotest.core.spec.style.StringSpec

class DefiningASoldier : StringSpec({

    "Scenario: Define a swordsman" {
        Given { a_soldier_with_a(meleeAttack(of_strength = 10)) }
            .And { it has_a meleeDefense(of_strength = 10) }
            .And { it has_a rangedDefense(of_strength = 4) }
            .And { it has 20.health }
            .And { it is_called_a "swordsman" }

        When { a_soldier_called_a("swordsman") is_added_to the(battle) }
            .And { the(battle).is_started }

        Then { there_should_be_soldier_called_a("swordsman") somewhere_in the(battle) }
            .And { it should_have_a meleeAttack(of_strength = 10) }
            .And { it should_have_a rangedDefense(of_strength = 4) }
            .And { it should_have_a meleeDefense(of_strength = 10) }
            .And { it should_have 20.health }
    }

    "Scenario: Define an archer" {
        Given { a_soldier_with_a(rangedAttack(of_strength = 12)) }
            .And { it has_a meleeDefense(of_strength = 4) }
            .And { it has_a rangedDefense(of_strength = 6) }
            .And { it has 15.health }
            .And { it is_called_a "archer" }

        When { a_soldier_called_a("archer") is_added_to the(battle) }
            .And { the(battle).is_started }

        Then { there_should_be_soldier_called_a("archer") somewhere_in the(battle) }
            .And { it should_have_a rangedAttack(of_strength = 12) }
            .And { it should_have_a rangedDefense(of_strength = 6) }
            .And { it should_have_a meleeDefense(of_strength = 4) }
            .And { it should_have 15.health }
    }

}) {

    companion object {
        fun createSwordsman() = Soldier("swordsman", false, SoldierHealth(10), null, listOf(
            meleeAttack(of_strength = 10),
            rangedDefense(of_strength = 4),
            meleeDefense(of_strength = 10)
        ))

        fun createArcher() = Soldier("archer", false, SoldierHealth(10), null, listOf(
            rangedAttack(of_strength = 12),
            rangedDefense(of_strength = 6),
            meleeDefense(of_strength = 4)
        ))
    }

}