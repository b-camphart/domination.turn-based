@file:Suppress("PackageDirectoryMismatch")
/*
 * All fixtures are in the domination.fixtures package to allow features to easily import everything via
 * [import domination.fixtures.*]
 */
package domination.fixtures

import domination.battle.Battle
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

fun Battle.shouldNotBeOver(): Battle {
    isOver.shouldBeFalse()
    return this
}

fun Battle.shouldBeOver(): Battle {
    isOver.shouldBeTrue()
    return this
}