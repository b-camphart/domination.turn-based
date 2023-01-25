package domination.battle

open class SoldierHealth(val value: Int) {

    operator fun minus(reduction: SoldierHealth) = SoldierHealth(value - reduction.value)
    operator fun minus(reduction: Int) = SoldierHealth(value - reduction)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SoldierHealth) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value
    }

    override fun toString(): String {
        return "SoldierHealth($value)"
    }


}