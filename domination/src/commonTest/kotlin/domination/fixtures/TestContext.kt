package domination.fixtures

import domination.fixtures.battle.ConfigurableDominationBattle
import domination.battle.DominationBattle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

val game: ConfigurableDominationBattle = ConfigurableDominationBattle(CoroutineScope(Dispatchers.Default))