package domination.battle

@JvmInline
value class SoldierHealth(val value: Int) {

    operator fun minus(reduction: SoldierHealth) = SoldierHealth(value - reduction.value)
    operator fun minus(reduction: Int) = SoldierHealth(value - reduction)

}