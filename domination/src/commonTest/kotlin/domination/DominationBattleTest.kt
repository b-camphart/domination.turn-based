package domination

import domination.battle.DominationBattle
import domination.usecases.battle.battleContextContract
import io.kotest.core.spec.style.FunSpec

class DominationBattleTest : FunSpec({
    include(battleContextContract(DominationBattle(mapOf())))
})