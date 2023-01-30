package domination.battle

data class SoldierAbility(val strength: Int, val name: String) {

    val isDefense: Boolean
        get() = name.contains("defense", ignoreCase = true)

}