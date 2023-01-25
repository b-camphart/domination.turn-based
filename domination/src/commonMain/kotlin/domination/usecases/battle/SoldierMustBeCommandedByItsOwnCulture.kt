package domination.usecases.battle

import domination.Culture

class SoldierMustBeCommandedByItsOwnCulture(
    val expectedCulture: Culture,
    val foundCulture: Culture?
) : Throwable() {

    override fun toString(): String {
        return "SoldierMustBeCommandedByItsOwnCulture(expectedCulture=$expectedCulture, foundCulture=$foundCulture)"
    }
}