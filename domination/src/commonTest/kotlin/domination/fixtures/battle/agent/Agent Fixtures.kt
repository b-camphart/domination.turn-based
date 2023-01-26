@file:Suppress("PackageDirectoryMismatch", "UnusedReceiverParameter")

/*
 * All fixtures are in the domination.fixtures package to allow features to easily import everything via
 * [import domination.fixtures.*]
 */
package domination.fixtures

import domination.Culture
import domination.battle.Agent

val player: Agent = Agent(Culture())
val enemy: Agent = Agent(Culture())
val other_enemy: Agent = Agent(Culture())