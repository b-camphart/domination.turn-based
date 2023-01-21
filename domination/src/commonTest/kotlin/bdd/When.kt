package bdd

@BddKeyword
suspend fun <T> When(def: suspend WhenScope.() -> T): WhenCont<T> {
    return WhenCont(WhenScope, WhenScope.def())
}

object WhenScope

class WhenCont<T>(private val scope: WhenScope, private val value: T) : Continuation<WhenScope, T> {

    @BddKeyword
    override suspend fun <R> And(def: WhenScope.(T) -> R): WhenCont<R> {
        return WhenCont(scope, scope.def(value))
    }

    @BddKeyword
    override suspend fun <R> But(def: WhenScope.(T) -> R): WhenCont<R> {
        return WhenCont(scope, scope.def(value))
    }

}