package domination.usecases.battle

import domination.battle.Battle
import domination.battle.BattleContext
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.shouldBe

fun battleContextContract(context: BattleContext) = funSpec {
    test("updating battle returns the value within the update") {

        context.updateBattle { battle, updateBattle -> 14 }
            .shouldBe(14)
    }

    test("updating battle allows future access") {
        val expectedBattle = Battle(emptyList())

        context.updateBattle { battle, updateBattle -> updateBattle(expectedBattle) }
        context.getBattle() shouldBe expectedBattle
        context.updateBattle { battle, _ -> battle shouldBe expectedBattle }

    }
}