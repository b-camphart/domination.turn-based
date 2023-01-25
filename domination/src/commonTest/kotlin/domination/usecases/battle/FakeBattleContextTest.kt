package domination.usecases.battle

import io.kotest.core.spec.style.FunSpec

class FakeBattleContextTest : FunSpec({
    include(battleContextContract(FakeBattleContext()))
})