package domination.fixtures

import domination.fixtures.battle.ConfigurableDominationBattle
import io.kotest.core.annotation.AutoScan
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.listeners.BeforeTestListener
import io.kotest.core.spec.IsolationMode
import io.kotest.core.test.TestCase


class KotestConfig : AbstractProjectConfig() {
    override val isolationMode: IsolationMode = IsolationMode.InstancePerLeaf
}

@Suppress("unused")
@AutoScan
object GameReset : BeforeTestListener {
    override suspend fun beforeTest(testCase: TestCase) {
        game = ConfigurableDominationBattle(playerCulture = player.culture)
    }
}