@file:Suppress("PackageDirectoryMismatch", "UnusedReceiverParameter")

/*
 * All fixtures are in the domination.fixtures package to allow features to easily import everything via
 * [import domination.fixtures.*]
 */
package domination.fixtures

import bdd.GivenScope
import bdd.ThenScope
import bdd.WhenScope
import domination.battle.Agent
import domination.fixtures.battle.agent.AgentActions
import domination.fixtures.battle.agent.AgentAssertions

fun GivenScope.the(agent: Agent) = agent

fun WhenScope.the(agent: Agent) = AgentActions(agent)

fun ThenScope.the(agent: Agent) = AgentAssertions(agent)